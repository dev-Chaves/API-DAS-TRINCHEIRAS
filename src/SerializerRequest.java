import java.util.Locale;

public class SerializerRequest {

    protected SerializerRequest(){

    }

    public static String serialize (ProdutoRequest json){

        String response = String.format(Locale.US, """
                {  \s
                    "id": %d,
                    "nome": "%s",
                    "preco": %.2f
                }
               \s""", json.id(), json.nome(), json.preco());

        return response;
    }

    public static ProdutoRequest desserialize (String json){

        Long id = null;
        String nome = null;
        Double preco = null;

        String jsonFormatadoo = json.replace(" ", "").replace("\r","").replace("\n","");

        String[] partes = jsonFormatadoo.replace(" ", "").split(",");

        for (String parte : partes) {

            String parametro = (parte.split(":")[0].trim().replace("}", "").replace("\"", "").replace("{",""));

            System.out.println("Chave para o switch: ->" + parametro + "<-");

            switch (parametro){
                case "id": id = Long.valueOf(parte.split(":")[1].trim().replace("}", ""));
                    break;
                case "nome": nome = parte.split(":")[1].trim().replace("}","").replace("\"", "");
                    break;
                case "preco": preco = Double.valueOf(parte.split(":")[1].trim().replace("}",""));
                    break;
            }

        }

        return new ProdutoRequest(id, nome, preco);

    }

}
