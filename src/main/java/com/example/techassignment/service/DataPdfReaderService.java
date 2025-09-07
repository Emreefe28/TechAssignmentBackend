package com.example.techassignment.service;

import com.example.techassignment.entity.LostItem;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DataPdfReaderService {

    @Autowired
    private LostItemService lostItemService;

    public String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setSortByPosition(true);
            pdfStripper.setAddMoreFormatting(true);
            return pdfStripper.getText(document);
        }
    }

    public List<LostItem> processLostItemsFromPdf(MultipartFile file) throws IOException {
        String extractedText = extractTextFromPdf(file);

        if (extractedText == null || extractedText.trim().isEmpty()) {
            throw new IllegalArgumentException("No text found in PDF");
        }

        return parseAndSaveItems(extractedText);
    }

    public List<LostItem> parseAndSaveItems(String text) {
        List<LostItem> savedItems = new ArrayList<>();

        // Use regex patterns for more efficient matching
        Pattern itemNamePattern = Pattern.compile("ItemName:\\s*(.+?)\\s*$", Pattern.MULTILINE);
        Pattern quantityPattern = Pattern.compile("Quantity:\\s*(\\d+)\\s*$", Pattern.MULTILINE);
        Pattern placePattern = Pattern.compile("Place:\\s*(.+?)\\s*$", Pattern.MULTILINE);

        // Extract all matches in one pass
        List<String> itemNames = extractMatches(text, itemNamePattern);
        List<Integer> quantities = extractQuantities(text, quantityPattern);
        List<String> places = extractMatches(text, placePattern);

        // Process items sequentially
        int itemCount = Math.min(Math.min(itemNames.size(), quantities.size()), places.size());

        for (int i = 0; i < itemCount; i++) {
            try {
                LostItem lostItem = createLostItem(itemNames.get(i), quantities.get(i), places.get(i));
                LostItem savedItem = lostItemService.saveLostItem(lostItem);
                savedItems.add(savedItem);
            } catch (Exception e) {
                System.err.println("Error saving item " + (i + 1) + ": " + e.getMessage());
            }
        }

        return savedItems;
    }

    public boolean isPdfValid(MultipartFile file) {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            return document.getNumberOfPages() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    public int getPageCount(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            return document.getNumberOfPages();
        }
    }

    private List<String> extractMatches(String text, Pattern pattern) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matches.add(matcher.group(1).trim());
        }
        return matches;
    }

    private List<Integer> extractQuantities(String text, Pattern pattern) {
        List<Integer> quantities = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            try {
                quantities.add(Integer.parseInt(matcher.group(1).trim()));
            } catch (NumberFormatException e) {
                System.err.println("Invalid quantity: " + matcher.group(1));
            }
        }
        return quantities;
    }

    private LostItem createLostItem(String itemName, Integer quantity, String place) {
        LostItem lostItem = new LostItem();
        lostItem.setItemName(itemName);
        lostItem.setQuantity(quantity);
        lostItem.setPlace(place);
        return lostItem;
    }
}