package net.iceyleagons.frostedengineering.other.web;

import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class AuthcodeGenerator {

    protected static UUID uuid;
    protected static KeyPair keyPair;
    protected static String authCode;
    protected static boolean init = false;

    public static void generate() {
        if (init) return;
        try {
            uuid = UUID.randomUUID();
            generateKeyPair(); //check for saved ones TODO	
            authCode = new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded())) + ";" + compressUUID(uuid);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        init = true;
    }

    private static void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA");
        SecureRandom r = SecureRandom.getInstance("SHA1PRNG", "SUN");
        kgen.initialize(512, r);

        keyPair = kgen.generateKeyPair();
    }

    public static String compressUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(Long.BYTES * 2);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        byte[] array = bb.array();
        return Base64.getEncoder().encodeToString(array);
    }
}
