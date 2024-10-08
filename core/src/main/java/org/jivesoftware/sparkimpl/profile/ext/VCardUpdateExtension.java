/**
 * Copyright (C) 2004-2011 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.sparkimpl.profile.ext;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.XmlEnvironment;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.xml.XmlPullParser;
import org.jivesoftware.smack.xml.XmlPullParserException;

import java.io.IOException;

public class VCardUpdateExtension implements ExtensionElement {

    public static final String ELEMENT_NAME = "x";

    public static final String NAMESPACE = "vcard-temp:x:update";

    private String photoHash;

    public void setPhotoHash(String hash) {
        photoHash = hash;
    }

    public String getPhotoHash() {
        return photoHash;
    }

    @Override
	public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
	public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String toXML(XmlEnvironment xmlEnvironment) {
        return "<" + getElementName() + " xmlns=\"" + getNamespace() + "\">"
                + "<photo>"
                + photoHash
                + "</photo>"
                + "</" + getElementName() + ">";
    }

    public static class Provider extends ExtensionElementProvider<VCardUpdateExtension>
    {
        public Provider() {
        }

        @Override
        public VCardUpdateExtension parse(XmlPullParser parser, int initialDepth, XmlEnvironment xmlEnvironment)
                throws XmlPullParserException, IOException {
            final VCardUpdateExtension result = new VCardUpdateExtension();

            while ( true )
            {
                parser.next();
                switch ( parser.getEventType() )
                {
                    case START_ELEMENT:
                        if ( "photo".equals( parser.getName() ) )
                        {
                            result.setPhotoHash( parser.nextText() );
                        }
                        break;

                    case END_ELEMENT:
                        if ( parser.getDepth() == initialDepth )
                        {
                            return result;
                        }
                        break;
                }
            }
        }
    }
}
