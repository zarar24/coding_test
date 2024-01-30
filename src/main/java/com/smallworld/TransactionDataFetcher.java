package com.smallworld;

import com.smallworld.data.Transaction;
import java.io.InputStream;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TransactionDataFetcher {


    private List<Transaction> transactions;

    public TransactionDataFetcher() {
        loadTransactions();
    }

    // Load transactions from the JSON file
    private void loadTransactions() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("transactions.json")) {
            Transaction[] transactionArray = objectMapper.readValue(inputStream, Transaction[].class);
            transactions = Arrays.asList(transactionArray);
        } catch (IOException e) {
            e.printStackTrace();
            transactions = Collections.emptyList();
        }
    }


    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return transactions.stream()
                .filter(transaction -> senderFullName.equals(transaction.getSenderFullName()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0.0);
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        Set<String> uniqueClients = transactions.stream()
                .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
                .collect(Collectors.toSet());
        return uniqueClients.size();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
                .anyMatch(transaction ->
                        clientFullName.equals(transaction.getSenderFullName()) ||
                                clientFullName.equals(transaction.getBeneficiaryFullName()) &&
                                        transaction.getIssueId() != null &&
                                        !transaction.isIssueSolved());
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        return transactions.stream()
                .collect(Collectors.toMap(Transaction::getBeneficiaryFullName, transaction -> transaction));

    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        return transactions.stream()
                .filter(transaction -> transaction.getIssueId() != null && !transaction.isIssueSolved())
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return transactions.stream()
                .filter(Transaction::isIssueSolved)
                .map(Transaction::getIssueMessage)
                .collect(Collectors.toList());
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(Transaction::getAmount)))
                .entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

}
