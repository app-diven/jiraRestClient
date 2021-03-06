/*
 * Copyright 2013 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.micromata.jira.rest;


import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import de.micromata.jira.rest.util.RestConstants;
import de.micromata.jira.rest.util.RestException;
import de.micromata.jira.rest.util.RestURIBuilder;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.UriBuilder;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * @author Christian Schulze
 * @author Vitali Filippow
 */
public class JiraRestClient {

    private ApacheHttpClient client;

    /**
     * The base URI.
     */
    private URI baseUri;

    private String username = StringUtils.EMPTY;


    public JiraRestClient() {
    }

    /**
     * Builds and configures a new client connection to JIRA.
     *
     * @param uri      = the login mask URI where JIRA is running
     * @param username = login name
     * @param password = login password
     * @throws RestException
     */
    public int connect(URI uri, String username, String password) {
        this.username = username;

        //Apache HTTP client setup
        ApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
        clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, Boolean.TRUE);
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION, Boolean.TRUE);
        clientConfig.getState().setCredentials(AuthScope.ANY_REALM, uri.getHost(), uri.getPort(), username, password);
        this.client = ApacheHttpClient.create(clientConfig);

        // Prüfen ob der Server überhaupt erreichbar ist
        ClientResponse clientResponse = client.resource(uri).get(ClientResponse.class);
        Status status = clientResponse.getClientResponseStatus();
        if (status.getFamily() == Family.CLIENT_ERROR || status.getFamily() == Family.SERVER_ERROR) {
            return status.getStatusCode();
        }
        // Prüfen ob der Nutzer sich anmelden kann indem wir seine nutzerdaten abrufen
        this.baseUri = UriBuilder.fromUri(uri).path(RestConstants.BASE_REST_PATH).build();
        URI uri1 = RestURIBuilder.buildGetUserByUsername(baseUri, username);
        clientResponse = this.client.resource(uri1).get(ClientResponse.class);
        status = clientResponse.getClientResponseStatus();
        if (status == Status.UNAUTHORIZED) {
            return status.getStatusCode();
        }
        clientResponse.close();
        return status.getStatusCode();
    }

    /**
     * Gets the Apache HTTP client.
     *
     * @return the client
     */
    public ApacheHttpClient getClient() {
        return client;
    }

    /**
     * Gets the base URI.
     *
     * @return the base URI
     */
    public URI getBaseUri() {
        return baseUri;
    }

    public String getUsername() {
        return username;
    }
}
