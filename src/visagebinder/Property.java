/*
 * Copyright (c) 2013, Visage Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name Visage nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package visagebinder;
/**
 *
 * @author Kalyan <kalyanthedeveloper@gmail.com>
 * @author SivaBalan <sivabalanb.92@gmail.com>
 */
public class Property {

    String propertyName;
    String propertyType;
    String defaultValue;
    String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String toString() {
        String propertyvalue = null;

        propertyvalue = "\tpublic var " + propertyName + " : " + propertyType + " on replace {\n"
                + "        \tif( isInitialized(this) or " + propertyName + " != " + defaultValue + " ) {\n"
                + "            \t\twrapped" + className + ".set" + indexUpperCase(propertyName) + "( " + propertyName + " );\n"
                + "        \t}\n"
                + "\t}";

        return propertyvalue;
    }

    public String indexUpperCase(String value) {
        char[] stringArray = value.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        value = new String(stringArray);
        return value;
    }

    public String getGettersName(String value) {
        return "get" + indexUpperCase(value);

    }
}
