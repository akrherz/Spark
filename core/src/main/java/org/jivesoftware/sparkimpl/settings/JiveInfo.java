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

package org.jivesoftware.sparkimpl.settings;


import org.jivesoftware.resource.SparkRes;

public class JiveInfo {

    private JiveInfo() {

    }
    
    public static String getName() {
        final String name = SparkRes.getString( "APP_NAME" );
        if ( name != null && !name.trim().isEmpty() )
        {
            return name.trim();
        }

        return "Spark";
    }

    public static String getVersion() {
        final String version = SparkRes.getString( "VERSION" );
        if ( version!= null && !version.trim().isEmpty() )
        {
            return version.trim();
        }

        return "3.0.3"; // avoid null and return at least some current version
    }

    public static String getOS() {
        return System.getProperty("os.name");
    }
}
