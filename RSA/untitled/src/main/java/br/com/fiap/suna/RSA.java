package br.com.fiap.suna;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RSA {

    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger m;
    private BigInteger e; // Chave pública
    private BigInteger d; // Chave privada

    /**
     * Construtor que gera as chaves RSA com base em números primos fixos.
     */
    public RSA() {
        p = new BigInteger("1009");
        q = new BigInteger("1013");

        // Calcula n = p * q (módulo)
        n = p.multiply(q);

        // Calcula a função totiente de Euler: phi(n) = (p - 1) * (q - 1)
        m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Escolhe um expoente 'e' tal que 1 < e < phi(n) e mdc(e, phi(n)) = 1
        e = new BigInteger("65537"); // Valor comum para 'e'
        while (m.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(m) < 0) {
            e = e.add(BigInteger.ONE);
        }

        // Calcula 'd', o inverso multiplicativo de 'e' módulo phi(n)
        d = e.modInverse(m);
    }

    // Criptografa uma mensagem usando a chave pública (e, n) de outra entidade.
    public static String criptografar(String mensagem, BigInteger eExterno, BigInteger nExterno) {
        byte[] msgBytes = mensagem.getBytes(StandardCharsets.UTF_8);
        StringBuilder cifradaStringBuilder = new StringBuilder();

        for (byte b : msgBytes) {
            BigInteger msgBigInt = new BigInteger(1, new byte[]{b});
            BigInteger cifrada = msgBigInt.modPow(eExterno, nExterno);
            cifradaStringBuilder.append(cifrada).append(" ");
        }
        return cifradaStringBuilder.toString().trim();
    }

    // Decifra uma mensagem usando a chave privada (d, n) local.
    public String decifrar(String mensagemCifrada) {
        String[] cifradaParts = mensagemCifrada.split(" ");
        StringBuilder decifradaStringBuilder = new StringBuilder();

        for (String part : cifradaParts) {
            if (part.isEmpty()) continue;
            BigInteger cifradaBigInt = new BigInteger(part);
            BigInteger decifrada = cifradaBigInt.modPow(d, n);
            decifradaStringBuilder.append((char) decifrada.intValue());
        }
        return decifradaStringBuilder.toString();
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getE() {
        return e;
    }
}