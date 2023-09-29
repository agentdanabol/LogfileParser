import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {

    List<Operation> operationList;

    public Parser() {
        this.operationList = new ArrayList<>();
    }

    public void parse(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            boolean inside = false;
            int tempId = -1;
            String line = reader.readLine();

            while (line!= null) {

                StringBuilder dateTimeString = new StringBuilder();
                for (int i = 0; line.charAt(i) != '['; ++i) {
                    dateTimeString.append(line.charAt(i));
                }

                String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                Date dateTime = dateFormat.parse(dateTimeString.toString());

                String statusLine = line.substring(line.indexOf(']') + 2);
                String[] words = statusLine.split(" ");

                switch (words[0]) {
                    case "Operation" -> {
                        if (words[words.length - 1].equals("started.")) {
                            int id = Integer.parseInt(words[2].substring(words[2].indexOf('=') + 1, words[2].indexOf(']')));
                            Operation operation = new Operation();
                            operation.setId(id);
                            operation.setOperationTime(dateTime.getTime());
                            operation.setStatus(true);
                            operationList.add(operation);
                            tempId = id;
                        }
                        if (words[words.length - 1].equals("finished.")) {
                            Operation operation = findById(tempId);
                            operation.setOperationTime(dateTime.getTime() - operation.getOperationTime());
                        }
                        if (words[words.length - 1].equals("failed.")) {
                            Operation operation = findById(tempId);
                            operation.setOperationTime(dateTime.getTime() - operation.getOperationTime());
                            operation.setStatus(false);
                        }
                    }
                    case "Authentication" -> {
                        Operation operation = findById(tempId);
                        if (inside) {
                            operation.setAuthenticationTime(dateTime.getTime() - operation.getAuthenticationTime());
                            inside = false;
                        } else {
                            operation.setAuthenticationTime(dateTime.getTime());
                            inside = true;
                        }
                    }
                    case "Authorization" -> {
                        Operation operation = findById(tempId);
                        if (inside) {
                            operation.setAuthorizationTime(dateTime.getTime() - operation.getAuthorizationTime());
                            inside = false;
                        } else {
                            operation.setAuthorizationTime(dateTime.getTime());
                            inside = true;
                        }
                    }
                    case "Balances" -> {
                        Operation operation = findById(tempId);
                        if (inside) {
                            operation.setBalanceModificationTime(dateTime.getTime() - operation.getBalanceModificationTime());
                            inside = false;
                        } else {
                            operation.setBalanceModificationTime(dateTime.getTime());
                            inside = true;
                        }
                    }
                    default -> throw new IllegalStateException("Illegal operation flow");
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Operation findById(int id) throws Exception {
        return operationList.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new Exception("Операция c id = " + id + "не найдена"));
    }

    public void printStats() {
        int successfulOp = 0;
        int avgSuccessfulTime = 0;

        int failedOp = 0;
        int avgFailedTime = 0;

        int avgSuccessfulAuthenticationTime = 0;
        int avgFailedAuthenticationTime = 0;
        int avgSuccessfulAuthorizationTime = 0;
        int avgFailedAuthorizationTime = 0;
        int avgSuccessfulBalanceTime = 0;
        int avgFailedBalanceTime = 0;

        for (Operation operation : operationList) {
            if (operation.isStatus()) {
                successfulOp++;
                avgSuccessfulTime += operation.getOperationTime();
                avgSuccessfulAuthenticationTime += operation.getAuthenticationTime();
                avgSuccessfulAuthorizationTime += operation.getAuthorizationTime();
                avgSuccessfulBalanceTime += operation.getBalanceModificationTime();
            } else {
                failedOp++;
                avgFailedTime += operation.getOperationTime();
                avgFailedAuthenticationTime += operation.getAuthenticationTime();
                avgFailedAuthorizationTime += operation.getAuthorizationTime();
                avgFailedBalanceTime += operation.getBalanceModificationTime();
            }
        }
        if (successfulOp != 0) {
            avgSuccessfulTime = avgSuccessfulTime / successfulOp;
            avgSuccessfulAuthenticationTime = avgSuccessfulAuthenticationTime / successfulOp;
            avgSuccessfulAuthorizationTime = avgSuccessfulAuthorizationTime / successfulOp;
            avgSuccessfulBalanceTime = avgSuccessfulBalanceTime / successfulOp;
        }
        if (failedOp != 0) {
            avgFailedTime = avgFailedTime / failedOp;
            avgFailedAuthenticationTime = avgFailedAuthenticationTime / failedOp;
            avgFailedAuthorizationTime = avgFailedAuthorizationTime / failedOp;
            avgFailedBalanceTime = avgFailedBalanceTime / failedOp;
        }

        System.out.println("Successful operation count: " + successfulOp);
        System.out.println("Average operation successful processing time: " + avgSuccessfulTime + "ms");
        System.out.println("\nFailed operation count: " + failedOp);
        System.out.println("Average operation failed processing time: " + avgFailedTime);
        System.out.println("\nAverage Authentication time (for successful operations): " + avgSuccessfulAuthenticationTime + "ms");
        System.out.println("Average Authentication time (for failed operations): " + avgFailedAuthenticationTime + "ms");
        System.out.println("\nAverage Authorization time (for successful operations): " + avgSuccessfulAuthorizationTime + "ms");
        System.out.println("Average Authorization time (for failed operations): " + avgFailedAuthorizationTime);
        System.out.println("\nAverage Balance modification time (for successful operations): " + avgSuccessfulBalanceTime + "ms");
        System.out.println("Average Balance modification time (for failed operations): " + avgFailedBalanceTime + "ms");
    }

}
