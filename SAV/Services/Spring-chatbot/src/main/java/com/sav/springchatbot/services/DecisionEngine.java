package com.sav.springchatbot.services;

import com.sav.springchatbot.utils.QuestionType;
import org.springframework.stereotype.Service;

@Service
public class DecisionEngine {

    public QuestionType analyse(String question) {
        String lower = question.toLowerCase();

        if (lower.contains("réclamation") ||
                lower.contains("traitement") ||
                lower.contains("état") ||
                lower.contains("status")||
                lower.contains("sav")) {
            return QuestionType.TYPE_RECLAMATION;
        }

        if (lower.contains("ne marche pas") || lower.contains("panne") || lower.contains("chauffe-eau") || lower.contains("guide")) {
            return QuestionType.TYPE_GUIDE;
        }

        return QuestionType.TYPE_GENERAL;
    }
}
