import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidateGymMembershipTest {

    ValidateGymMembership testObject = new ValidateGymMembership();

    @Test
    void readFromFileTest() throws IOException {
        List<String> tempList = new ArrayList<>();
        BufferedReader instream = new BufferedReader(new FileReader("test/testFile.txt"));
        String line="";

        while ((line = instream.readLine()) != null){
            line = line.toLowerCase();
            tempList.add(line);
        }
        instream.close();

        assertEquals(6, tempList.size());
        assertEquals("1234567891, anna andersson", tempList.get(0));
        assertEquals("2020-01-01", tempList.get(1));
        assertEquals("3456789123, bob bodilsson", tempList.get(2));
        assertFalse(tempList.isEmpty());
        assertNotEquals("1234567891, Anna Andersson", tempList.get(0));

    }

    @Test
    void doesMethodReadFromFileWorkTest(){
        List<String> tempList = testObject.createListOfMembersFromFile("test/testFile.txt");

        assertTrue(tempList.get(2).equals("3456789123, bob bodilsson"));
    }

    @Test
    void divideMemberDataInListTest(){
        List<String> tempList = testObject.createListOfMembersFromFile("test/testFile.txt");
        String nameOfMember;
        String personNummer;
        String dateMemberLastPaid;
        int index;

        String member = tempList.get(0);
        index = member.indexOf(',');
        nameOfMember = member.substring(index+2);
        personNummer = member.substring(0,index);

        dateMemberLastPaid = tempList.get(0+1);

        assertTrue(nameOfMember.equals("anna andersson"));
        assertTrue(personNummer.equals("1234567891"));
        assertTrue(dateMemberLastPaid.equals("2020-01-01"));

    }

    @Test
    void shouldTakeUserInputTest() {
        String input = "anna andersson";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertTrue(testObject.getReceptionistInput().equals(input));
    }

    @Test
    void capitalizeMembersNameTest(){
        String name = "anna andersson";
        int findLastName = name.indexOf(" ");

        String firstLetter = name.substring(0,1).toUpperCase();
        String remLetters = name.substring(1,findLastName);

        String secondNameLetter = name.substring(findLastName+1, findLastName+2).toUpperCase();
        String remLettersLastname = name.substring(findLastName+2);

        String fullname = firstLetter + remLetters + " " + secondNameLetter + remLettersLastname;

        assertEquals("Anna Andersson", fullname);
        assertFalse(fullname.equals("anna andersson"));
    }

    @Test
    void checkIfInputExistsInListTest() {
        List<String> tempList = testObject.createListOfMembersFromFile("test/testFile.txt");
        String indataTest = "5678912345";
        for (int i=0; i < tempList.size(); i++){
            if (tempList.get(i).contains(indataTest)){
                assertTrue(tempList.get(i).equals("5678912345, carla carrington"));
            } else {
                assertFalse(tempList.get(i).contains(indataTest));
            }
        }

    }

    @Test
    void calculateIfMemberPaidAnnualFeeTest() {
        LocalDate today = LocalDate.now();

        String hasPaidFee = "2021-01-01";
        LocalDate membersPaidFee1 = LocalDate.parse(hasPaidFee);

        String hasNotPaidFee = "2020-01-01";
        LocalDate membersPaidFee2 = LocalDate.parse(hasNotPaidFee);

        Period paidFee = Period.between(membersPaidFee1, today);
        Period notPaidFee = Period.between(membersPaidFee2, today);

        assertTrue(paidFee.getYears() == 0);
        assertTrue(notPaidFee.getYears() == 1);
        assertFalse(notPaidFee.getYears() == 0);

    }

    @Test
    void createPersonalFileForTrainerTest() throws IOException {
        String name= "Arnold";
        String nameOfFile = name + ".txt";
        String personNummer = "XXXXXX-XXXX";
        String membershipLastPaid = "2021-01-01";
        String date = "2021-10-09";

        PrintWriter outstream = new PrintWriter(new BufferedWriter(new FileWriter(nameOfFile, true)));

        outstream.println(name + " " + personNummer + " " + membershipLastPaid);
        outstream.println(date);
        outstream.close();

        List<String> tempList = testObject.createListOfMembersFromFile(nameOfFile);
        assertTrue(tempList.get(0).equals("arnold xxxxxx-xxxx 2021-01-01"));
        assertFalse(tempList.get(1).equals("2020-10-09"));
        assertTrue(tempList.get(1).equals(date));

    }
}