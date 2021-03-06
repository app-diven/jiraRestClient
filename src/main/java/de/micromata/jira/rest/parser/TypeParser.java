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
import de.micromata.jira.rest.domain.TypeBean;
import de.micromata.jira.rest.util.JsonElementUtil;


/**
 * @author Christian Schulze
 * @author Vitali Filippow
 */
public class TypeParser extends BaseParser {

    public static TypeBean parse(JsonObject object) {
        TypeBean bean = new TypeBean();
        parseBaseProperties(bean, object);
        JsonElement inwardElement = object.get(PROP_INWARD);
        if (JsonElementUtil.checkNotNull(inwardElement) == true) {
            String iw = inwardElement.getAsString();
            bean.setInward(iw);
        }

        JsonElement outwardElement = object.get(PROP_OUTWARD);
        if (JsonElementUtil.checkNotNull(outwardElement) == true) {
            String ow = outwardElement.getAsString();
            bean.setOutward(ow);
        }
        return bean;
    }

}
