package kclc.common;

public class Common {

    /** will provide a random alphanumeric characters, this will be used to name items that must be unique (e.g. profile pic)
     * ***/
    public static String randomAlphanumeric(){

        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        java.util.Random random = new java.util.Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

                //System.out.println(generatedString);
        return generatedString;
    }
}
