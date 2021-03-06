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

package de.micromata.jira.rest.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.micromata.jira.rest.domain.VotesBean;
import de.micromata.jira.rest.util.JsonElementUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christian Schulze
 * @author Vitali Filippow
 */
public class VotesParser extends BaseParser {

    public static VotesBean parse(JsonObject object) {
        VotesBean bean = new VotesBean();
        parseBaseProperties(bean, object);
        JsonElement votesElement = object.get(PROP_VOTES);
        if (JsonElementUtil.checkNotNull(votesElement) == true) {
            int votes = votesElement.getAsInt();
            bean.setVotes(votes);
        }
        JsonElement hasVotedElement = object.get(PROP_HAS_VOTED);
        if (JsonElementUtil.checkNotNull(hasVotedElement) == true) {
            boolean hasVoted = hasVotedElement.getAsBoolean();
            bean.setHasVoted(hasVoted);
        }
        return bean;
    }

    public static List<VotesBean> parse(List<JsonObject> objects) {
        List<VotesBean> retval = new ArrayList<VotesBean>();
        for (JsonObject object : objects) {
            VotesBean votesBean = parse(object);
            retval.add(votesBean);
        }
        return retval;
    }
}
