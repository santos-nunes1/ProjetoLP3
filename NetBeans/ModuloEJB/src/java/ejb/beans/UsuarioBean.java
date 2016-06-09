package ejb.beans;

import ejb.entities.Usuario;
import ejb.interceptor.LogInterceptor;
import ejb.utils.Hash;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean

public class UsuarioBean {

    @PersistenceContext(unitName = "DerbyPU")
    private EntityManager em;

    public void save(Usuario u) {
        em.persist(u);
    }

    public List<Usuario> list() {
        Query query = em.createQuery("FROM Usuario u");
        List<Usuario> list = query.getResultList();
        return list;
    }

    public boolean validaSenha(String nome, String senha) {

        try {
            Hash hash = new Hash();
            StringBuffer sb = hash.obterHash(senha);

            Query query = em.createQuery("FROM Usuario u where u.login = :login AND u.password = :password");
            query.setParameter("login", nome);
            query.setParameter("password", sb.toString());
            List lista = query.getResultList();
            if (lista.isEmpty()) {
                return false;

            } else {
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    @Interceptors({LogInterceptor.class})
    public void ErroAutenticacao() {
        return;
    }

    @Interceptors({LogInterceptor.class})
    public void SucessoAutenticacao() {
        return;
    }

    @Interceptors({LogInterceptor.class})
    public void ListaDeUsuarios() {
        return;
    }

    @Interceptors({LogInterceptor.class})
    public void CadastroDeUsuarios() {
        return;
    }

}
