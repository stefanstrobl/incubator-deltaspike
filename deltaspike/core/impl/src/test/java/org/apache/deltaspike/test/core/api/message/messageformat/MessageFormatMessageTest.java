/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.deltaspike.test.core.api.message.messageformat;

import org.apache.deltaspike.core.impl.message.MessageBundleExtension;
import org.apache.deltaspike.core.impl.message.MessageFormatMessageInterpolator;
import org.apache.deltaspike.test.category.SeCategory;
import org.apache.deltaspike.test.core.api.message.locale.ConfigurableLocaleResolver;
import org.apache.deltaspike.test.util.ArchiveUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link MessageFormatMessageInterpolator}
 */
@RunWith(Arquillian.class)
@Category(SeCategory.class)
public class MessageFormatMessageTest
{
    private static final String BEANS_XML_CONTENT =
            "<beans><alternatives><class>" +
            ConfigurableLocaleResolver.class.getName() +
            "</class><class>" +
            MessageFormatMessageInterpolator.class.getName() +
            "</class></alternatives></beans>";

    @Inject
    private MessageWithMessageFormat messages;

    @Inject
    private ConfigurableLocaleResolver localeResolver;

    /**
     * X TODO creating a WebArchive is only a workaround because JavaArchive
     * cannot contain other archives.
     */
    @Deployment
    public static WebArchive deploy()
    {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "messageFormatMessageTest.jar")
                .addPackage(MessageFormatMessageTest.class.getPackage())
                .addPackage(ConfigurableLocaleResolver.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        return ShrinkWrap
                .create(WebArchive.class, "messageFormatMessageTest.war")
                .addAsLibraries(ArchiveUtils.getDeltaSpikeCoreArchive())
                .addAsLibraries(testJar)
                .addAsWebInfResource(new StringAsset(BEANS_XML_CONTENT), "beans.xml")
                .addAsServiceProvider(Extension.class,
                        MessageBundleExtension.class);
    }

    @Test
    public void testSimpleMessage()
    {
        assertEquals("Welcome to DeltaSpike", messages.welcomeToDeltaSpike());
        assertEquals("Welcome to DeltaSpike", messages.welcomeWithStringVariable("DeltaSpike"));
    }

    @Test
    public void testChoice() {
        int number = 3;
        String expectedResult = new MessageFormat("{0,choice,0#zero persons|1#one person|1<{0} persons}").format(new Object[]{number});
        String result = messages.messageWithChoice(number);
        assertEquals(expectedResult, result);
    }

    /**
     * This test checks if the {@link org.apache.deltaspike.core.api.message.LocaleResolver}
     * gets properly invoked.
     */
    @Test
    public void testDefaultLocaleInMessage()
    {
        internalTestLocales(Locale.GERMANY);
        internalTestLocales(Locale.US);
        internalTestLocales(Locale.FRANCE);
    }

    private void internalTestLocales(Locale locale)
    {

        localeResolver.setLocale(locale);

        Float f = 123.45f;
        String expectedResult = "Welcome " + new MessageFormat("{0}", locale).format(new Object[]{f});
        String result = messages.welcomeWithFloatVariable(f);
        assertEquals(expectedResult, result);


        Date dt = new Date();
        expectedResult = "Welcome " + new MessageFormat("{0,date}", locale).format(new Object[]{dt});
        result = messages.welcomeWithDateVariable(dt);
        assertEquals(expectedResult, result);

    }
}
