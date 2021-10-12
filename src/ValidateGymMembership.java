import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ValidateGymMembership {
    private final String filePath = "src/customers.txt";

    private String input;
    private String fullNameOfMember;
    private String personalIdentityNumber;
    private LocalDate membershipDate;
    private boolean userInputExist;

    public List<String> createListOfMembersFromFile(String filePath){
        List<String> listOfGymMembers = new ArrayList<>();
        String memberInfo;

        try(BufferedReader instream = new BufferedReader(new FileReader(filePath))){
            while ((memberInfo = instream.readLine()) != null){
                memberInfo = memberInfo.toLowerCase();
                listOfGymMembers.add(memberInfo);
            }

        } catch (FileNotFoundException e){
            System.out.println("File not found..." + e.getMessage());
            System.exit(0);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }

        return listOfGymMembers;
    }

    public String getReceptionistInput(){
        Scanner sc = new Scanner(System.in);
        boolean inputIsEmpty = true;

        while (inputIsEmpty){
            System.out.println("Enter members Full name or Personal Identity number: ");
            this.input = sc.nextLine();
            if (this.input.isEmpty()){
                System.out.println("Field cannot be empty, try again");
            } else if (this.input.length() < 2){ //keep or not
                System.out.println("Please try again, too few characters");
            } else {
                this.input = this.input.toLowerCase().trim();
                inputIsEmpty = false;
            }
        }

        return this.input;
    }

    public void checkIfInputExists(List<String> listOfGymMembers, String input) {
        if (listOfGymMembers.isEmpty()){
            System.out.println("List given is empty or invalid, try another.");
            System.exit(0);
        }

        userInputExist = false;
        for (int i = 0; i < listOfGymMembers.size(); i++) {
            if (listOfGymMembers.get(i).contains(input)) {

                String extractMemberLastPaidDate = listOfGymMembers.get(i+1);
                this.membershipDate = LocalDate.parse(extractMemberLastPaidDate);

                String member = listOfGymMembers.get(i);
                int divideMemberInfo = member.indexOf(',');
                this.fullNameOfMember = member.substring(divideMemberInfo+2);
                this.personalIdentityNumber = member.substring(0, divideMemberInfo);

                validateMemberStatus(input);
            }
        }

        if (!userInputExist){
            System.out.println("Not a member at the gym.");
        }

    }

    public void validateMemberStatus(String input){
        if (input.equals(this.fullNameOfMember) || input.equals(this.personalIdentityNumber)){

            this.userInputExist = true;

            capitalizeMembersName(this.fullNameOfMember);
            System.out.println("Info: " + this.fullNameOfMember + ", " + this.personalIdentityNumber +
                    ", " + this.membershipDate);

            LocalDate today = LocalDate.now();
            Period checkIfMemberPaidAnnualFee = Period.between(this.membershipDate, today); //hasMember,

            if (checkIfMemberPaidAnnualFee.getYears() == 0) {
                System.out.println("Current member, membership is valid");

                String textFileName = this.fullNameOfMember + ".txt";
                createPersonalFileForTrainer(this.personalIdentityNumber, this.fullNameOfMember,
                        this.membershipDate, textFileName);
            } else {
                System.out.println("Membership is not valid anymore");
                System.out.println(checkIfMemberPaidAnnualFee.getYears() + " years since " + this.fullNameOfMember +
                        " paid for their membership");
            }

        }
    }

    public void createPersonalFileForTrainer(String personalIdentityNumber,
                                             String fullNameOfMember, LocalDate membershipDate, String textFileName) {

        boolean exists = new File(textFileName).exists();

        try(PrintWriter outstream = new PrintWriter(new BufferedWriter(new FileWriter(textFileName, true)))){
            LocalDate today = LocalDate.now();

            if (!exists){
                outstream.println("Name: " + fullNameOfMember + " Personal Identity Number: " + personalIdentityNumber +
                        " Annual fee paid: " + membershipDate);
            }

            outstream.println(today);

        } catch (IOException e){
            System.out.println("Problem writing to file");
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }


    public String getFilePath() {
        return filePath;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public String getFullNameOfMember() {
        return fullNameOfMember;
    }

    public String getPersonalIdentityNumber() {
        return personalIdentityNumber;
    }

    public void capitalizeMembersName(String nameOfMember){
        int findLastName = nameOfMember.indexOf(" ");

        String firstLetter = nameOfMember.substring(0,1).toUpperCase();
        String remainingLetters = nameOfMember.substring(1,findLastName);

        String lastNameLetter = nameOfMember.substring(findLastName+1, findLastName+2).toUpperCase();
        String remLettersLastname = nameOfMember.substring(findLastName+2);

        this.fullNameOfMember = firstLetter + remainingLetters + " " + lastNameLetter + remLettersLastname;
    }

}
