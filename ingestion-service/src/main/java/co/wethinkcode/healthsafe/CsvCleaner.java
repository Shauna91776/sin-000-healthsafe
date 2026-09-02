package co.wethinkcode.healthsafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CsvCleaner {

    public List<Ward> clean() {

        List<Ward> wards = new ArrayList<>();
        Set<String> seenWardIds = new HashSet<>();

        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("wards-outdated.csv")) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Could not find wards-outdated.csv"
                );
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream)
            );

            String line;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(",", -1);

                String wardId = fields[0].trim().toUpperCase();
                String wing = cleanName(fields[1]);
                String department = cleanName(fields[2]);

                String bedsValue = fields[3].trim();

                Integer bedsAvailable = null;
                String notes = "";

                // Duplicate check
                if (seenWardIds.contains(wardId)) {
                    notes = "Duplicate ward ID: " + wardId;
                } else {
                    seenWardIds.add(wardId);
                }

                // Beds validation
                if (bedsValue.equalsIgnoreCase("N/A")
                        || bedsValue.equalsIgnoreCase("TBD")
                        || bedsValue.equalsIgnoreCase("unknown")
                        || bedsValue.isEmpty()) {

                    notes = addNote(
                            notes,
                            "bedsAvailable is missing or a placeholder"
                    );

                } else {

                    try {

                        int beds = Integer.parseInt(bedsValue);

                        if (beds < 0) {

                            notes = addNote(
                                    notes,
                                    "bedsAvailable was negative"
                            );

                        } else {

                            bedsAvailable = beds;
                        }

                    } catch (NumberFormatException e) {

                        notes = addNote(
                                notes,
                                "bedsAvailable was non-numeric ('"
                                        + bedsValue + "')"
                        );
                    }
                }

                Ward ward = new Ward(
                        wardId,
                        wing,
                        department,
                        bedsAvailable,
                        notes
                );

                wards.add(ward);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error reading wards-outdated.csv", e
            );
        }

        return wards;
    }

    private String cleanName(String value) {

        value = value.trim();

        if (value.isEmpty()) {
            return value;
        }

        value = value.replaceAll("\\s+", " ");
        value = value.toLowerCase();

        String[] words = value.split(" ");

        for (int i = 0; i < words.length; i++) {
            words[i] = Character.toUpperCase(words[i].charAt(0))
                    + words[i].substring(1);
        }

        return String.join(" ", words);
    }

    private String addNote(String existingNotes, String newNote) {

        if (existingNotes.isEmpty()) {
            return newNote;
        }

        return existingNotes + "; " + newNote;
    }
}