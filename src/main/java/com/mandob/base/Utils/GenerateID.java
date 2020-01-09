package com.mandob.base.Utils;

import org.hashids.Hashids;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GenerateID implements IdentifierGenerator {

    private final static String ALPHABET = "Aa0Bb1Cc2Dd3Ee4Ff5G6Hh7Ii8J9KkLlMmNnOPQRrSsTtUuVvWwXxYZz";
    private static volatile AtomicLong COUNTER = new AtomicLong(System.currentTimeMillis()/1000);
    private static volatile Hashids HASH_IDS = new Hashids(UUID.randomUUID().toString(), 9, ALPHABET);

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return HASH_IDS.encode(COUNTER.incrementAndGet());
    }

    public static void main(String[] args) {
        GenerateID id = new GenerateID();
        Scanner scanner = new Scanner(System.in);
        int Number_of_ids = scanner.nextInt();
        for (int i = 1 ; i <= Number_of_ids ; i++)
            System.out.println(id.generate(null,null));
    }


}
