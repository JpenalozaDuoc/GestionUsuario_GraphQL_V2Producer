package userRest.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import userRest.connection.UserDAO;

public class UserMutationResolver implements GraphQLMutationResolver{

     // Mutación: Crear usuario
    public boolean createUser(String name, String email, String password) {
        boolean result = UserDAO.createUser(name, email, password);
        return result;
    }

    // Mutación: Actualizar usuario
    public boolean updateUser(Long id, String name, String email, String password) {
        boolean result = UserDAO.updateUser(id, name, email, password);
        return result;
    }

    // Mutación: Eliminar usuario
    public boolean deleteUser(Long id) {
        boolean result = UserDAO.deleteUser(id);
        return result;
    }
}