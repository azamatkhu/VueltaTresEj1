import java.sql.*;
import java.util.*;

public class MainVueltaTresEj1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("Escribe un numero de etapa: ");
        int etapa = sc.nextInt();
        sc.nextLine();

        System.out.println("Escribe el origen de etapa: ");
        String origen = sc.nextLine();

        System.out.println("Escribe el destino de etapa: ");
        String destino = sc.nextLine();

        System.out.println("Escribe la distancia de etapa: ");
        int distancia = sc.nextInt();
        sc.nextLine();

        System.out.println("Escribe la fecha de etapa: ");
        String fecha = sc.nextLine();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe",
                "RIBERA",
                "ribera"
        )){
            System.out.println("Conectado!");

            connection.setAutoCommit(false);
            try {
                String sqlInsertarEtap = "INSERT INTO ETAPA (NUMERO, ORIGEN, DESTINO, DISTANCIA_KM, FECHA) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertarEtap);
                preparedStatement.setInt(1, etapa);
                preparedStatement.setString(2, origen);
                preparedStatement.setString(3, destino);
                preparedStatement.setInt(4, distancia);
                preparedStatement.setDate(5, java.sql.Date.valueOf(fecha));

                preparedStatement.executeUpdate();

                int numeroDeCiclistas = 0;

                String sqlCiclistas = "SELECT COUNT(*) FROM CICLISTA";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlCiclistas);

                if (resultSet.next()) {
                    numeroDeCiclistas = resultSet.getInt(1);
                }

                String sqlParticipacion = "INSERT INTO PARTICIPACION(numero_etapa, id_ciclista, posicion, puntos) VALUES (?, ?, ?, ?)";
                Set<Integer> posiciones = new LinkedHashSet<>();

                while (posiciones.size() < numeroDeCiclistas) {
                    posiciones.add(rand.nextInt(0, numeroDeCiclistas) + 1);
                }

                List<Integer> listaPosiciones = new ArrayList<>(posiciones);

                for (int i = 0; i < numeroDeCiclistas; i++) {
                    preparedStatement = connection.prepareStatement(sqlParticipacion);

                    preparedStatement.setInt(1, etapa);
                    preparedStatement.setInt(2, i + 1);
                    int posicion = listaPosiciones.get(i);
                    preparedStatement.setInt(3, posicion);

                    switch (posicion) {
                        case 1:
                            preparedStatement.setInt(4, 100);
                            break;
                        case 2:
                            preparedStatement.setInt(4, 90);
                            break;
                        case 3:
                            preparedStatement.setInt(4, 80);
                            break;
                        case 4:
                            preparedStatement.setInt(4, 70);
                            break;
                        case 5:
                            preparedStatement.setInt(4, 60);
                            break;
                        default:
                            preparedStatement.setInt(4, 0);
                            break;
                    }

                    preparedStatement.executeUpdate();
                }
                connection.commit();
                System.out.println("Se añadido la etapa!");
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}