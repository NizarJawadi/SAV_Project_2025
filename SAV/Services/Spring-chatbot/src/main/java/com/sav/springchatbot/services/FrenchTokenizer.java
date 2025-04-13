package com.sav.springchatbot.services;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


import java.io.FileInputStream;
import java.io.InputStream;

import java.io.IOException;

public class FrenchTokenizer {

    private Tokenizer tokenizer;

    public FrenchTokenizer(String modelPath) throws Exception {
        // Charger le modèle OpenNLP pour la tokenisation en français à partir du classpath
        InputStream modelIn = getClass().getClassLoader().getResourceAsStream(modelPath);
        if (modelIn == null) {
            throw new IOException("Modèle introuvable dans le classpath : " + modelPath);
        }
        TokenizerModel model = new TokenizerModel(modelIn);
        this.tokenizer = new TokenizerME(model);
    }

    public String[] tokenize(String sentence) {
        // Utiliser le tokenizer pour diviser la phrase en tokens
        return tokenizer.tokenize(sentence);
    }


    public static void main(String[] args) {
        try {
            // Détection des phrases
            try (InputStream modelIn = FrenchTokenizer.class.getClassLoader().getResourceAsStream("static/models/opennlp-fr-ud-gsd-sentence-1.2-2.5.0.bin")) {

                SentenceModel sentenceModel = new SentenceModel(modelIn);
                SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);

                String paragraph = "Bonjour ! Comment allez-vous ? Je vais bien, merci.";
                String[] sentences = sentenceDetector.sentDetect(paragraph);

                FrenchTokenizer tokenizer = new FrenchTokenizer("static/models/opennlp-fr-ud-gsd-tokens-1.2-2.5.0.bin"); // Exemple de nom pour le modèle de tokenisation

                for (String sentence : sentences) {
                    System.out.println("Phrase : " + sentence);
                    String[] tokens = tokenizer.tokenize(sentence);
                    System.out.println("Tokens : [" + String.join(" | ", tokens) + "]");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
