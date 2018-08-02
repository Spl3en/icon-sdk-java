/*
 * Copyright 2018 theloop Inc.
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

package foundation.icon.icx;

import foundation.icon.icx.crypto.IconKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.CipherException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyWalletTest {

    public static final String PRIVATE_KEY_STRING =
            "2d42994b2f7735bbc93a3e64381864d06747e574aa94655c516f9ad0a74eed79";

    public static final String PASSWORD = "Pa55w0rd";
    public File tempDir;

    private static File createTempDir() throws Exception {
        return Files.createTempDirectory(
                "testkeys").toFile();
    }

    @BeforeEach
    void setUp() throws Exception {
        tempDir = createTempDir();
    }

    @Test
    public void testLoadWithPrivateKey() {
        KeyWallet wallet = KeyWallet.load(PRIVATE_KEY_STRING);
        assertEquals("hx4873b94352c8c1f3b2f09aaeccea31ce9e90bd31", wallet.getAddress());
    }

    @Test
    public void testCreate() {
        KeyWallet wallet = KeyWallet.create();
        assertTrue(IconKeys.isValidAddress(wallet.getAddress()));
        Wallet loadWallet = KeyWallet.load(wallet.getEcKeyPair().getPrivateKey().toString(16));
        assertEquals(wallet.getAddress(), loadWallet.getAddress());
    }

    @Test
    public void testKeyStore() throws CipherException, IOException {
        KeyWallet wallet = KeyWallet.load(PRIVATE_KEY_STRING);
        String fileName = KeyWallet.store(wallet, PASSWORD, tempDir);
        Matcher matcher = Pattern.compile("hx[0-9a-fA-F]{40}").matcher(fileName);
        if (matcher.find()) {
            assertEquals(wallet.getAddress(), matcher.group());
        }
    }

}