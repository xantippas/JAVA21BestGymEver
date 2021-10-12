import java.util.List;

public class Main {

    public static void main(String[] args) {
        ValidateGymMembership checkMembership = new ValidateGymMembership();

        List<String> listOfMembers = checkMembership.createListOfMembersFromFile(checkMembership.getFilePath());
        String input = checkMembership.getReceptionistInput();

        try{
            checkMembership.checkIfInputExists(listOfMembers, input);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
