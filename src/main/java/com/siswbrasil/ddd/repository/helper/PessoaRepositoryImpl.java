package com.siswbrasil.ddd.repository.helper;

import com.siswbrasil.ddd.modelo.Pessoa;
import com.siswbrasil.ddd.repository.filtro.PessoaFiltro;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PessoaRepositoryImpl implements PessoaRepositoryQueries {

    @PersistenceContext
    EntityManager manager;

    @Override
    public List<Pessoa> filtrar(PessoaFiltro filtro) {

        final  StringBuilder sb = new StringBuilder();
        final Map<String,Object> params = new HashMap<>();

        sb.append("SELECT bean FROM Pessoa bean WHERE 1=1");

        preencherNomeSeNecessario(filtro, sb, params);

        preencherCpfSeNecessario(filtro, sb, params);

        Query query = manager.createQuery(sb.toString(),Pessoa.class);
        preencherParametrosDaQuery(params, query);

        return query.getResultList();
    }

    private void preencherParametrosDaQuery(Map<String, Object> params, Query query) {
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
    }

    private void preencherNomeSeNecessario(PessoaFiltro filtro, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(filtro.getNome())){
            sb.append(" AND bean.nome LIKE :nome ");
            params.put("nome","%" + filtro.getNome() + "%");
        }
    }

    private void preencherCpfSeNecessario(PessoaFiltro filtro, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(filtro.getCpf())){
            sb.append(" AND bean.cpf LIKE :cpf ");
            params.put("cpf","%" + filtro.getCpf() + "%");
        }
    }
}
