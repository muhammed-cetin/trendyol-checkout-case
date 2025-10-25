package com.trendyol;

import com.trendyol.command.CommandRequest;
import com.trendyol.command.CommandResponse;
import com.trendyol.domain.exception.CartException;
import com.trendyol.domain.exception.ItemException;
import com.trendyol.io.InputReader;
import com.trendyol.io.JsonMapper;
import com.trendyol.io.OutputWriter;
import com.trendyol.service.CartService;

import java.util.List;

public class Application {

    private static final String DEFAULT_INPUT_FILE = "input.txt";
    private static final String DEFAULT_OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        // Determine file paths
        String inputFile = args.length > 0 ? args[0] : DEFAULT_INPUT_FILE;
        String outputFile = args.length > 1 ? args[1] : DEFAULT_OUTPUT_FILE;

        // Run the application
        Application app = new Application();
        app.run(inputFile, outputFile);
    }

    public void run(String inputFilePath, String outputFilePath) {

        InputReader inputReader = new InputReader();
        OutputWriter outputWriter = new OutputWriter();
        JsonMapper jsonMapper = new JsonMapper();
        CartService cartService = new CartService();

        // Clear output file
        outputWriter.clearFile(outputFilePath);

        try {
            // Read input file
            List<String> inputLines = inputReader.readLines(inputFilePath);

            // Process each row
            for (String line : inputLines) {
                CommandResponse response = processCommand(line, jsonMapper, cartService);

                // Convert response to JSON and write to file
                String responseJson = jsonMapper.toJson(response);
                outputWriter.writeLine(outputFilePath, responseJson);
            }

            System.out.println("✓ Processing completed successfully!");
            System.out.println("  Input file: " + inputFilePath);
            System.out.println("  Output file: " + outputFilePath);

        } catch (Exception e) {
            System.err.println("✗ Application error: " + e.getMessage());
            System.err.println("  Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "Unknown"));
        }
    }

    private CommandResponse processCommand(String jsonLine, JsonMapper jsonMapper, CartService cartService) {
        try {
            // Parse JSON
            CommandRequest request = jsonMapper.parseRequest(jsonLine);
            String command = request.getCommand();

            // Run the command
            return switch (command) {
                case "addItem" -> handleAddItem(request, cartService);
                case "addVasItemToItem" -> handleAddVasItemToItem(request, cartService);
                case "removeItem" -> handleRemoveItem(request, cartService);
                case "resetCart" -> handleResetCart(cartService);
                case "displayCart" -> handleDisplayCart(cartService);
                default -> CommandResponse.error("Unknown command: " + command);
            };

        } catch (CartException | ItemException e) {
            return CommandResponse.error(e.getMessage());
        } catch (Exception e) {
            return CommandResponse.error("Error processing command: " + e.getMessage());
        }
    }

    private CommandResponse handleAddItem(CommandRequest request, CartService cartService) {
        Integer itemId = request.getIntValue("itemId");
        Integer categoryId = request.getIntValue("categoryId");
        Integer sellerId = request.getIntValue("sellerId");
        Double price = request.getDoubleValue("price");
        Integer quantity = request.getIntValue("quantity");

        if (itemId == null || categoryId == null || sellerId == null ||
                price == null || quantity == null) {
            return CommandResponse.error("Missing required parameters for addItem");
        }

        cartService.addItem(itemId, categoryId, sellerId, price, quantity);
        return CommandResponse.success("Item added to cart successfully");
    }

    private CommandResponse handleAddVasItemToItem(CommandRequest request, CartService cartService) {
        Integer itemId = request.getIntValue("itemId");
        Integer vasItemId = request.getIntValue("vasItemId");
        Integer vasCategoryId = request.getIntValue("vasCategoryId");
        Integer vasSellerId = request.getIntValue("vasSellerId");
        Double price = request.getDoubleValue("price");
        Integer quantity = request.getIntValue("quantity");

        if (itemId == null || vasItemId == null || vasCategoryId == null ||
                vasSellerId == null || price == null || quantity == null) {
            return CommandResponse.error("Missing required parameters for addVasItemToItem");
        }

        cartService.addVasItemToItem(itemId, vasItemId, vasCategoryId, vasSellerId, price, quantity);
        return CommandResponse.success("VasItem added to item successfully");
    }

    private CommandResponse handleRemoveItem(CommandRequest request, CartService cartService) {
        Integer itemId = request.getIntValue("itemId");

        if (itemId == null) {
            return CommandResponse.error("Missing required parameter: itemId");
        }

        boolean removed = cartService.removeItem(itemId);
        if (removed) {
            return CommandResponse.success("Item removed from cart successfully");
        } else {
            return CommandResponse.error("Item not found in cart");
        }
    }

    private CommandResponse handleResetCart(CartService cartService) {
        cartService.resetCart();
        return CommandResponse.success("Cart reset successfully");
    }

    private CommandResponse handleDisplayCart(CartService cartService) {
        return CommandResponse.displayCart(cartService.getCart());
    }
}