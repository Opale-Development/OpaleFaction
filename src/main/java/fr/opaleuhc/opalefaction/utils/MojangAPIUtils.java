package fr.opaleuhc.opalefaction.utils;

public class MojangAPIUtils {

    /*private static final String UUID_URL = "https://api.mojang.com/user/profile/";

    private CompletableFuture<String> getName(UUID uuid) {
        String name;
        BufferedReader in;

        CompletableFuture<String> future = new CompletableFuture<>();

        try {
            in = new BufferedReader(new InputStreamReader(new URL(UUID_URL + uuid.toString()).openStream()));
            name = (((JsonObject)new JsonParser().parse(in)).get("name")).toString().replaceAll("\"", "");
            //uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            in.close();
            future.complete(name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while getting name from UUID : " + uuid.toString());
            future.completeExceptionally(e);
        }
        return future;
    }

    public static void test() {
        long start = System.currentTimeMillis();
        UUID uuid = UUID.fromString("f09cd989-3fd7-43b2-95b0-6f9dfab11793");
        CompletableFuture<String> future = new MojangAPIUtils().getName(uuid);
        future.thenAccept(name -> {
            System.out.println("Name : " + name);
            System.out.println("Time : " + (System.currentTimeMillis() - start));
        });
    }*/

}
