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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kalyan <kalyanthedeveloper@gmail.com>
 * @author SivaBalan <sivabalanb.92@gmail.com>
 * @author Rajmahendra <rajmahendra@gmail.com>
 */
public class Visagebinder {

    public static void main(String[] args) {

        InputStream in = (InputStream) Visagebinder.class.getResourceAsStream("packages.txt");
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader b = new BufferedReader(isr);

        Class<?> c = null;
        String className = null;
        try {
            while ((className = b.readLine()) != null) {
                c = Class.forName(className);
                //CustomClass Contains whole blueprint of the Class
                CustomClass customclass = new CustomClass(c.getSimpleName(), c.getPackage().getName(), c.getSuperclass().getSimpleName(), c.getSuperclass().getPackage().getName(), new ArrayList<String>());
                Field[] fields = c.getDeclaredFields();
                //intialise all the fields to customclass
                initFields(fields, c, customclass);
                System.out.println(customclass);
                File dir = new File(customclass.getPackageName().replace('.', '/'));
                dir.mkdirs();
                File f = new File(dir, customclass.getClassName() + ".visage");
                FileWriter fw = new FileWriter(f);
                fw.write(customclass.toString());
                fw.close();
            }
            
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Visagebinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public void initFields(Field[] fields, Class c, CustomClass customclass) {
        for (Field field : fields) {
            //Initialise the property name and data type
            Property p = new Property();
            p.setPropertyName(field.getName());
            p.setClassName(c.getSimpleName());
            p.setPropertyType(getGenericTypeName(field));
           

            if (Modifier.isFinal(field.getModifiers()) == false) {
                try {
                    //create getter name
                    String gettersName = p.getGettersName(p.getPropertyName());
                    //Get Default Value by creating new object
                    Method thisMethod = c.getDeclaredMethod(gettersName, null);
                    Object defaultValue = thisMethod.invoke(c.cast(c.newInstance()), null);
                    if (defaultValue != null) {
                        p.setDefaultValue(defaultValue.toString());
                    }

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    if (ex instanceof NoSuchMethodException) {
                        continue;
                    } else {
                        Logger.getLogger(Visagebinder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            customclass.getPropertyList().add(p.toString());

        }
    }

    public static String getGenericTypeName(Field field) {
         if (javafx.beans.property.Property.class.isAssignableFrom(field.getType())) {
                Type type = (Type) field.getGenericType();
                if (type instanceof ParameterizedType) {
                    //field is a parameterized generic type
                    ParameterizedType gentype = (ParameterizedType) type;
                    Class<?> integerListClass = (Class<?>) gentype.getActualTypeArguments()[0];
                    String gentypeName = integerListClass.getName();
                    String typeName = field.getType().getSimpleName()+"<"+gentypeName+">";
                    return typeName;
                }else if(field.getType().getSimpleName().endsWith("Property")){
                    //its a primitive type Property field
                    return field.getType().getSimpleName().replace("Property", "");                            
                }                   
            }
         //normal variable
        return field.getType().getSimpleName();
    }

    public static void println(String message) {
        System.out.println(message);
    }
}
