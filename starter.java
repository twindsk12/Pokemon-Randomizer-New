import java.util.*;
import java.io.*;

public class starter {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Hello, welcome to the Pokemon Randomizer, the ideal randomizer for Pokemon team generation, their natures, and their entries.");

            String choice;
            do {
                System.out.println("Do you want to choose multiple generations? (yes/no):");
                choice = sc.nextLine();
                if (choice.equalsIgnoreCase("draco")) {
                    printChampFile();
                    return;
                } else if (!choice.equalsIgnoreCase("yes") && !choice.equalsIgnoreCase("no")) {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            } while (!choice.equalsIgnoreCase("yes") && !choice.equalsIgnoreCase("no"));

            int teamSize;
            while (true) {
                System.out.println("How many Pokémon do you want in your team? (1-6):");
                teamSize = sc.nextInt();
                sc.nextLine();
                if (teamSize >= 1 && teamSize <= 6) {
                    break;
                } else {
                    System.out.println("Invalid team size. Please choose a number between 1 and 6.");
                }
            }

            if (choice.equalsIgnoreCase("yes")) {
                System.out.println("Please enter how many generations you want to choose:");
                int numGenerations = sc.nextInt();
                sc.nextLine();
                List<String> generations = readGenerations(sc, numGenerations);

                String typeChoice;
                do {
                    System.out.println("Do you want to filter by Pokémon type? (yes/no):");
                    typeChoice = sc.nextLine();
                    if (!typeChoice.equalsIgnoreCase("yes") && !typeChoice.equalsIgnoreCase("no")) {
                        System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                    }
                } while (!typeChoice.equalsIgnoreCase("yes") && !typeChoice.equalsIgnoreCase("no"));

                if (typeChoice.equalsIgnoreCase("yes")) {
                    System.out.println("Please enter the desired Pokémon type (Normal, Fire, Water, Electric, Grass, Ice, Fighting, Poison, Ground, Flying, Psychic, Bug, Rock, Ghost, Dragon, Dark, Steel, Fairy):");
                    String type = sc.nextLine();
                    generateRandomTeamByType(generations, type, teamSize);
                } else {
                    generateRandomTeamFromAllGenerations(generations, teamSize);
                }
            } else if (choice.equalsIgnoreCase("no")) {
                System.out.println("Please enter the name of the Pokemon generation file you want to generate a team for (gen1.txt, gen2.txt, gen3.txt, gen4.txt, gen5.txt, gen6.txt, gen7.txt, gen8.txt, gen9.txt):");
                String fileName = sc.nextLine();
                List<String> singleGeneration = new ArrayList<>();
                singleGeneration.add(fileName);

                String typeChoice;
                do {
                    System.out.println("Do you want to filter by Pokémon type? (yes/no):");
                    typeChoice = sc.nextLine();
                    if (!typeChoice.equalsIgnoreCase("yes") && !typeChoice.equalsIgnoreCase("no")) {
                        System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                    }
                } while (!typeChoice.equalsIgnoreCase("yes") && !typeChoice.equalsIgnoreCase("no"));

                if (typeChoice.equalsIgnoreCase("yes")) {
                    System.out.println("Please enter the desired Pokémon type (Normal, Fire, Water, Electric, Grass, Ice, Fighting, Poison, Ground, Flying, Psychic, Bug, Rock, Ghost, Dragon, Dark, Steel, Fairy):");
                    String type = sc.nextLine();
                    generateRandomTeamByType(singleGeneration, type, teamSize);
                } else {
                    generateRandomTeam(singleGeneration, teamSize);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static List<String> readGenerations(Scanner sc, int numGenerations) {
        List<String> generations = new ArrayList<>();
        for (int i = 0; i < numGenerations; i++) {
            System.out.println("Please enter the name of the Pokemon generation file for Generation " + (i + 1) + " (e.g., gen1.txt):");
            String fileName = sc.nextLine();
            generations.add(fileName);
        }
        return generations;
    }

    public static void generateRandomTeam(List<String> generations, int teamSize) throws FileNotFoundException {
        List<String> natures = readNatures();

        for (String fileName : generations) {
            List<String> pokemonList = readPokemonData(fileName);

            if (pokemonList.size() < teamSize) {
                System.out.println("Insufficient Pokémon data in " + fileName + " to generate a team.");
                continue;
            }

            List<String> randomTeam = new ArrayList<>();

            System.out.println("Random team from " + fileName + ":");
            while (randomTeam.size() < teamSize) {
                int randomIndex = (int) (Math.random() * pokemonList.size());
                String randomPokemon = pokemonList.get(randomIndex);
                String nature = natures.get((int) (Math.random() * natures.size()));
                randomTeam.add(randomPokemon + " (" + nature + ")");
                System.out.println("Added to team: " + randomPokemon + " (" + nature + ")");
            }
            System.out.println();

            List<String> weakTo = findTypesWeakTo(randomTeam);
            if (!weakTo.isEmpty()) {
                System.out.println("Types you're weak to:");
                for (String type : weakTo) {
                    System.out.println(type);
                }
            } else {
                System.out.println("You have no weaknesses!");
            }
        }
    }

    public static void generateRandomTeamFromAllGenerations(List<String> generations, int teamSize) throws FileNotFoundException {
        List<String> natures = readNatures();
        List<String> allPokemonList = new ArrayList<>();
        for (String fileName : generations) {
            allPokemonList.addAll(readPokemonData(fileName));
        }
        if (allPokemonList.size() < teamSize) {
            System.out.println("Insufficient Pokémon data to generate a team.");
            return;
        }
        List<String> randomTeam = new ArrayList<>();
        System.out.println("Random team from all generations:");
        generateTeam(allPokemonList, natures, randomTeam, teamSize);
        System.out.println();

        List<String> weakTo = findTypesWeakTo(randomTeam);
        if (!weakTo.isEmpty()) {
            System.out.println("Types you're weak to:");
            for (String type : weakTo) {
                System.out.println(type);
            }
        } else {
            System.out.println("You have no weaknesses!");
        }
    }

    private static void generateTeam(List<String> allPokemonList, List<String> natures, List<String> randomTeam, int teamSize) {
        if (randomTeam.size() >= teamSize) {
            return;
        }
        int randomIndex = (int) (Math.random() * allPokemonList.size());
        String randomPokemon = allPokemonList.get(randomIndex);
        if (!randomTeam.contains(randomPokemon)) {
            String nature = natures.get((int) (Math.random() * natures.size()));
            randomTeam.add(randomPokemon + " (" + nature + ")");
            System.out.println("Added to team: " + randomPokemon + " (" + nature + ")");
        }
        generateTeam(allPokemonList, natures, randomTeam, teamSize);
    }

    public static void generateRandomTeamByType(List<String> generations, String type, int teamSize) throws FileNotFoundException {
        List<String> natures = readNatures();
        List<String> filteredPokemonList = new ArrayList<>();

        for (String fileName : generations) {
            List<String> pokemonList = readPokemonData(fileName);
            for (String pokemon : pokemonList) {
                if (pokemon.contains("," + type + ",") || pokemon.endsWith("," + type)) {
                    filteredPokemonList.add(pokemon);
                }
            }
        }

        if (filteredPokemonList.size() < teamSize) {
            System.out.println("Insufficient Pokémon data with the type " + type + " to generate a team.");
            return;
        }

        List<String> randomTeam = new ArrayList<>();

        System.out.println("Random team of type " + type + " from all generations:");
        while (randomTeam.size() < teamSize) {
            int randomIndex = (int) (Math.random() * filteredPokemonList.size());
            String randomPokemon = filteredPokemonList.get(randomIndex);
            String nature = natures.get((int) (Math.random() * natures.size()));
            randomTeam.add(randomPokemon + " (" + nature + ")");
            System.out.println("Added to team: " + randomPokemon + " (" + nature + ")");
        }
        System.out.println();

        List<String> weakTo = findTypesWeakTo(randomTeam);
        if (!weakTo.isEmpty()) {
            System.out.println("Types you're weak to:");
            for (String weakType : weakTo) {
                System.out.println(weakType);
            }
        } else {
            System.out.println("You have no weaknesses!");
        }
    }

    public static List<String> readPokemonData(String fileName) throws FileNotFoundException {
        List<String> pokemonList = new ArrayList<>();
        File file = new File("pokemon_data/" + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("The file " + fileName + " does not exist.");
        }

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            pokemonList.add(sc.nextLine());
        }
        sc.close();
        return pokemonList;
    }

    public static List<String> readNatures() throws FileNotFoundException {
        List<String> natures = new ArrayList<>();
        File file = new File("nature.txt");
        if (!file.exists()) {
            throw new FileNotFoundException("The file nature.txt does not exist.");
        }

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            natures.add(sc.nextLine());
        }
        sc.close();
        return natures;
    }

    public static void printChampFile() throws FileNotFoundException {
        File file = new File("champ.txt");
        if (!file.exists()) {
            throw new FileNotFoundException("The file champ.txt does not exist.");
        }

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }
        sc.close();
    }

    private static Map<String, List<String>> initializeTypeWeaknesses() {
        Map<String, List<String>> typeWeaknesses = new HashMap<>();
        
        typeWeaknesses.put("Normal", List.of("Fighting"));
        typeWeaknesses.put("Fire", List.of("Water", "Ground", "Rock"));
        typeWeaknesses.put("Water", List.of("Electric", "Grass"));
        typeWeaknesses.put("Electric", List.of("Ground"));
        typeWeaknesses.put("Grass", List.of("Fire", "Ice", "Poison", "Flying", "Bug"));
        typeWeaknesses.put("Ice", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Fighting", List.of("Flying", "Psychic", "Fairy"));
        typeWeaknesses.put("Poison", List.of("Ground", "Psychic"));
        typeWeaknesses.put("Ground", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Psychic", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Bug", List.of("Fire", "Flying", "Rock"));
        typeWeaknesses.put("Rock", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Ghost", List.of("Ghost", "Dark"));
        typeWeaknesses.put("Dragon", List.of("Ice", "Dragon", "Fairy"));
        typeWeaknesses.put("Dark", List.of("Fighting", "Bug", "Fairy"));
        typeWeaknesses.put("Steel", List.of("Fire", "Fighting", "Ground"));
        typeWeaknesses.put("Fairy", List.of("Poison", "Steel"));
        
        typeWeaknesses.put("Normal,Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Normal,Poison", List.of("Ground", "Psychic"));
        typeWeaknesses.put("Normal,Steel", List.of("Fire", "Fighting", "Ground"));
        typeWeaknesses.put("Normal,Fairy", List.of("Fighting"));

        typeWeaknesses.put("Fire,Water", List.of("Ground", "Rock"));
        typeWeaknesses.put("Fire,Electric", List.of("Water", "Ground", "Rock"));
        typeWeaknesses.put("Fire,Ice", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Fire,Fairy", List.of("Water", "Ground", "Rock"));

        typeWeaknesses.put("Water,Electric", List.of("Grass"));
        typeWeaknesses.put("Water,Ice", List.of("Electric", "Grass"));
        typeWeaknesses.put("Water,Steel", List.of("Electric"));

        typeWeaknesses.put("Electric,Ground", List.of());
        
        typeWeaknesses.put("Grass,Fire", List.of("Fire", "Ice", "Poison", "Flying", "Bug"));
        typeWeaknesses.put("Grass,Poison", List.of("Ground", "Psychic"));
        typeWeaknesses.put("Grass,Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Grass,Bug", List.of("Fire", "Flying", "Rock"));
        typeWeaknesses.put("Grass,Steel", List.of("Fire", "Fighting", "Ground"));

        typeWeaknesses.put("Ice,Fire", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Fighting", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Rock", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Ice,Steel", List.of("Fire", "Fighting", "Ground"));

        typeWeaknesses.put("Fighting,Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Fighting,Psychic", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Fighting,Fairy", List.of("Psychic"));

        typeWeaknesses.put("Poison,Ground", List.of("Ground", "Psychic"));
        typeWeaknesses.put("Poison,Poison", List.of("Ground", "Psychic"));

        typeWeaknesses.put("Ground,Water", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Ice", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Grass", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Ground,Bug", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));

        typeWeaknesses.put("Flying,Electric", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Flying,Ice", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Flying,Rock", List.of("Electric", "Ice", "Rock"));

        typeWeaknesses.put("Psychic,Bug", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Ghost", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Dark", List.of("Bug", "Ghost", "Dark"));

        typeWeaknesses.put("Bug,Fire", List.of("Fire", "Flying", "Rock"));
        typeWeaknesses.put("Bug,Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Bug,Rock", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));

        typeWeaknesses.put("Rock,Water", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Rock,Grass", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Rock,Fighting", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Rock,Ground", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Rock,Steel", List.of("Fire", "Fighting", "Ground"));

        typeWeaknesses.put("Ghost,Ghost", List.of("Ghost", "Dark"));
        typeWeaknesses.put("Ghost,Dark", List.of("Ghost", "Dark"));

        typeWeaknesses.put("Dragon,Ice", List.of("Ice", "Dragon", "Fairy"));
        typeWeaknesses.put("Dragon,Dragon", List.of("Ice", "Dragon", "Fairy"));
        typeWeaknesses.put("Dragon,Fairy", List.of("Ice", "Dragon", "Fairy"));

        typeWeaknesses.put("Dark,Fighting", List.of("Fighting", "Bug", "Fairy"));
        typeWeaknesses.put("Dark,Bug", List.of("Fighting", "Bug", "Fairy"));
        typeWeaknesses.put("Dark,Fairy", List.of("Fighting", "Bug", "Fairy"));

        typeWeaknesses.put("Steel,Fire", List.of("Fire", "Fighting", "Ground"));
        typeWeaknesses.put("Steel,Fighting", List.of("Fire", "Fighting", "Ground"));
        typeWeaknesses.put("Steel,Ground", List.of("Fire", "Fighting", "Ground"));

        typeWeaknesses.put("Fairy,Poison", List.of("Poison", "Steel"));
        typeWeaknesses.put("Fairy,Steel", List.of("Poison", "Steel"));
        
        typeWeaknesses.put("Normal,Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Normal,Poison", List.of("Ground", "Psychic"));
        typeWeaknesses.put("Normal,Steel", List.of("Fire", "Fighting", "Ground"));
        typeWeaknesses.put("Normal,Fairy", List.of("Fighting"));

        typeWeaknesses.put("Fire,Water", List.of("Water", "Ground", "Rock"));
        typeWeaknesses.put("Fire,Grass", List.of("Water", "Rock"));
        typeWeaknesses.put("Fire,Ice", List.of("Water", "Ground", "Rock", "Fighting", "Steel"));
        typeWeaknesses.put("Fire,Bug", List.of("Water", "Rock", "Flying"));
        typeWeaknesses.put("Fire,Rock", List.of("Water", "Rock", "Ground"));
        typeWeaknesses.put("Fire,Dragon", List.of("Water", "Ground", "Rock"));

        typeWeaknesses.put("Water,Grass", List.of("Electric", "Grass"));
        typeWeaknesses.put("Water,Electric", List.of("Ground"));
        typeWeaknesses.put("Water,Ice", List.of("Electric", "Grass"));
        typeWeaknesses.put("Water,Fighting", List.of("Electric", "Grass"));
        typeWeaknesses.put("Water,Ground", List.of("Electric", "Grass"));
        typeWeaknesses.put("Water,Psychic", List.of("Electric", "Grass"));
        typeWeaknesses.put("Water,Bug", List.of("Electric", "Grass"));
        typeWeaknesses.put("Water,Rock", List.of("Electric", "Grass"));

        typeWeaknesses.put("Electric,Grass", List.of("Ground"));
        typeWeaknesses.put("Electric,Fighting", List.of("Ground"));
        typeWeaknesses.put("Electric,Ground", List.of("Ground"));
        typeWeaknesses.put("Electric,Poison", List.of("Ground"));
        typeWeaknesses.put("Electric,Flying", List.of("Ground"));
        typeWeaknesses.put("Electric,Psychic", List.of("Ground"));
        typeWeaknesses.put("Electric,Bug", List.of("Ground"));
        typeWeaknesses.put("Electric,Rock", List.of("Ground"));

        typeWeaknesses.put("Grass,Fire", List.of("Fire", "Ice", "Poison", "Flying", "Bug"));
        typeWeaknesses.put("Grass,Water", List.of("Electric", "Grass"));
        typeWeaknesses.put("Grass,Electric", List.of("Electric", "Grass"));
        typeWeaknesses.put("Grass,Ice", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Grass,Poison", List.of("Fire", "Flying", "Psychic", "Ice"));
        typeWeaknesses.put("Grass,Ground", List.of("Fire", "Flying", "Ice", "Bug"));
        typeWeaknesses.put("Grass,Flying", List.of("Fire", "Flying", "Rock", "Bug"));
        typeWeaknesses.put("Grass,Bug", List.of("Fire", "Flying", "Poison", "Bug"));
        typeWeaknesses.put("Grass,Rock", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Grass,Ghost", List.of("Ghost", "Dark"));
        typeWeaknesses.put("Grass,Dragon", List.of("Ice", "Dragon", "Fairy"));
        typeWeaknesses.put("Grass,Dark", List.of("Fighting", "Bug", "Fairy"));
        typeWeaknesses.put("Grass,Fairy", List.of("Poison", "Steel"));

        typeWeaknesses.put("Ice,Fire", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Water", List.of("Electric", "Grass"));
        typeWeaknesses.put("Ice,Electric", List.of("Ground"));
        typeWeaknesses.put("Ice,Fighting", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Poison", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Ground", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Ice,Psychic", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Bug", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Rock", List.of("Fire", "Fighting", "Rock", "Steel"));
        typeWeaknesses.put("Ice,Ghost", List.of("Ghost", "Dark"));
        typeWeaknesses.put("Ice,Dragon", List.of("Ice", "Dragon", "Fairy"));
        typeWeaknesses.put("Ice,Dark", List.of("Fighting", "Bug", "Fairy"));
        typeWeaknesses.put("Ice,Fairy", List.of("Poison", "Steel"));

        typeWeaknesses.put("Fighting,Flying", List.of("Electric", "Ice", "Psychic", "Fairy"));
        typeWeaknesses.put("Fighting,Poison", List.of("Ground", "Psychic", "Fairy"));
        typeWeaknesses.put("Fighting,Ground", List.of("Water", "Ice", "Grass", "Flying", "Bug", "Fairy"));
        typeWeaknesses.put("Fighting,Psychic", List.of("Bug", "Ghost", "Dark", "Fairy"));
        typeWeaknesses.put("Fighting,Bug", List.of("Fire", "Flying", "Psychic", "Fairy"));
        typeWeaknesses.put("Fighting,Rock", List.of("Water", "Grass", "Fighting", "Ground", "Steel", "Fairy"));
        typeWeaknesses.put("Fighting,Ghost", List.of("Ghost", "Dark", "Fairy"));
        typeWeaknesses.put("Fighting,Dragon", List.of("Ice", "Dragon", "Fairy"));
        typeWeaknesses.put("Fighting,Dark", List.of("Fighting", "Bug", "Fairy"));
        typeWeaknesses.put("Fighting,Steel", List.of("Fire", "Fighting", "Ground", "Fairy"));
        typeWeaknesses.put("Fighting,Fairy", List.of("Poison", "Steel"));

        typeWeaknesses.put("Poison,Grass", List.of("Fire", "Ice", "Flying", "Psychic"));
        typeWeaknesses.put("Poison,Ground", List.of("Water", "Ice", "Grass", "Flying", "Psychic"));
        typeWeaknesses.put("Poison,Flying", List.of("Electric", "Ice", "Psychic"));
        typeWeaknesses.put("Poison,Psychic", List.of("Ground", "Psychic"));
        typeWeaknesses.put("Poison,Bug", List.of("Fire", "Flying", "Psychic"));
        typeWeaknesses.put("Poison,Rock", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Poison,Ghost", List.of("Ground", "Psychic", "Ghost", "Dark"));
        typeWeaknesses.put("Poison,Dragon", List.of("Ground", "Psychic", "Ice", "Dragon", "Fairy"));
        typeWeaknesses.put("Poison,Dark", List.of("Ground", "Psychic", "Fighting", "Bug"));
        typeWeaknesses.put("Poison,Steel", List.of("Fire", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Poison,Fairy", List.of("Ground", "Psychic"));

        typeWeaknesses.put("Ground,Fire", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Electric", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Fighting", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Poison", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Flying", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Ground,Psychic", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Bug", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Rock", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
        typeWeaknesses.put("Ground,Ghost", List.of("Ice", "Grass", "Water", "Flying", "Bug"));
        typeWeaknesses.put("Ground,Dragon", List.of("Ice", "Dragon", "Fairy"));
        typeWeaknesses.put("Ground,Dark", List.of("Water", "Ice", "Grass"));
        typeWeaknesses.put("Ground,Fairy", List.of("Ice", "Grass", "Water"));

        typeWeaknesses.put("Flying,Electric", List.of("Ice", "Rock"));
        typeWeaknesses.put("Flying,Poison", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Flying,Fighting", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Flying,Ground", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Flying,Psychic", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Flying,Bug", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Flying,Rock", List.of("Electric", "Ice", "Rock"));
        typeWeaknesses.put("Flying,Ghost", List.of("Electric", "Ice", "Rock", "Ghost", "Dark"));
        typeWeaknesses.put("Flying,Dragon", List.of("Electric", "Ice", "Rock", "Dragon", "Fairy"));
        typeWeaknesses.put("Flying,Dark", List.of("Electric", "Ice", "Rock", "Ghost", "Dark"));
        typeWeaknesses.put("Flying,Fairy", List.of("Electric", "Ice", "Rock"));

        typeWeaknesses.put("Psychic,Grass", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Electric", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Fighting", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Ground", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Flying", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Bug", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Rock", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Ghost", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Dragon", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Dark", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Steel", List.of("Bug", "Ghost", "Dark"));
        typeWeaknesses.put("Psychic,Fairy", List.of("Bug", "Ghost", "Dark"));

    typeWeaknesses.put("Bug,Grass", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Electric", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Fighting", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Ground", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Flying", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Psychic", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Rock", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Bug,Ghost", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Dragon", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Dark", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Steel", List.of("Fire", "Flying", "Rock"));
    typeWeaknesses.put("Bug,Fairy", List.of("Fire", "Flying", "Rock"));

    typeWeaknesses.put("Rock,Fire", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Water", List.of("Grass", "Electric"));
    typeWeaknesses.put("Rock,Electric", List.of("Water", "Grass"));
    typeWeaknesses.put("Rock,Grass", List.of("Water", "Grass"));
    typeWeaknesses.put("Rock,Ice", List.of("Fire", "Fighting", "Rock", "Steel"));
    typeWeaknesses.put("Rock,Fighting", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Poison", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Ground", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Flying", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Psychic", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Bug", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Ghost", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Dragon", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Dark", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));
    typeWeaknesses.put("Rock,Fairy", List.of("Water", "Grass", "Fighting", "Ground", "Steel"));

    typeWeaknesses.put("Ghost,Normal", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Grass", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Electric", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Fighting", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Poison", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Ground", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Flying", List.of("Electric", "Ice", "Rock", "Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Psychic", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Bug", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Rock", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Dragon", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Dark", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Steel", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Ghost,Fairy", List.of("Ghost", "Dark"));

    typeWeaknesses.put("Dragon,Fire", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Water", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Electric", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Grass", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Ice", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Fighting", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Poison", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Ground", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Flying", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Psychic", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Bug", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Rock", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Ghost", List.of("Ghost", "Dark", "Fairy"));
    typeWeaknesses.put("Dragon,Dark", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Steel", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dragon,Fairy", List.of("Ice", "Dragon", "Fairy"));

    typeWeaknesses.put("Dark,Psychic", List.of("Fighting", "Bug", "Fairy"));
    typeWeaknesses.put("Dark,Bug", List.of("Fighting", "Bug", "Fairy"));
    typeWeaknesses.put("Dark,Rock", List.of("Fighting", "Bug", "Fairy"));
    typeWeaknesses.put("Dark,Ghost", List.of("Ghost", "Dark"));
    typeWeaknesses.put("Dark,Dragon", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Dark,Steel", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Dark,Fairy", List.of("Poison", "Steel"));

    typeWeaknesses.put("Steel,Fire", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Water", List.of("Electric", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Electric", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Ice", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Fighting", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Poison", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Ground", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Flying", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Psychic", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Bug", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Rock", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Ghost", List.of("Fire", "Fighting", "Ground"));
    typeWeaknesses.put("Steel,Dragon", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Steel,Fairy", List.of("Fire", "Fighting", "Ground"));

    typeWeaknesses.put("Fairy,Fighting", List.of("Poison", "Steel"));
    typeWeaknesses.put("Fairy,Poison", List.of("Steel"));
    typeWeaknesses.put("Fairy,Ground", List.of("Poison", "Steel"));
    typeWeaknesses.put("Fairy,Flying", List.of("Poison", "Steel"));
    typeWeaknesses.put("Fairy,Rock", List.of("Poison", "Steel"));
    typeWeaknesses.put("Fairy,Bug", List.of("Fire", "Steel"));
    typeWeaknesses.put("Fairy,Ghost", List.of("Ghost", "Steel"));
    typeWeaknesses.put("Fairy,Dragon", List.of("Ice", "Dragon", "Fairy"));
    typeWeaknesses.put("Fairy,Steel", List.of("Fire", "Fighting", "Ground"));

        return typeWeaknesses;
    }

    public static List<String> findTypesWeakTo(List<String> team) {
        Map<String, List<String>> typeWeaknesses = initializeTypeWeaknesses();

        List<String> weakTo = new ArrayList<>();
        for (String pokemon : team) {
            for (Map.Entry<String, List<String>> entry : typeWeaknesses.entrySet()) {
                String types = entry.getKey();
                List<String> weaknesses = entry.getValue();
                boolean isWeak = true;
                for (String type : types.split(",")) {
                    if (!pokemon.contains(type)) {
                        isWeak = false;
                        break;
                    }
                }
                if (isWeak) {
                    for (String weakness : weaknesses) {
                        if (!weakTo.contains(weakness)) {
                            weakTo.add(weakness);
                        }
                    }
                }
            }
        }
        return weakTo;
    }
}

