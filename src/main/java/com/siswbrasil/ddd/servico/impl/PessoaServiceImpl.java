package com.siswbrasil.ddd.servico.impl;

import com.siswbrasil.ddd.modelo.Pessoa;
import com.siswbrasil.ddd.modelo.Telefone;
import com.siswbrasil.ddd.repository.PessoaRepository;
import com.siswbrasil.ddd.servico.PessoaService;
import com.siswbrasil.ddd.servico.exception.TelefoneNaoEncontradoException;
import com.siswbrasil.ddd.servico.exception.UnicidadeCpfException;
import com.siswbrasil.ddd.servico.exception.UnicidadeTelefoneException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PessoaServiceImpl implements PessoaService {
    private final PessoaRepository pessoaRepository;

    public PessoaServiceImpl(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }


    @Override
    public Pessoa salvar(Pessoa pessoa) throws UnicidadeCpfException, UnicidadeTelefoneException {

        Optional<Pessoa> optional = pessoaRepository.findByCpf(pessoa.getCpf());

        if (optional.isPresent()) {
            throw new UnicidadeCpfException("Já existe pessoa cadastrada com o CPF '" + pessoa.getCpf() + "'");
        }

        String ddd = pessoa.getTelefones().get(0).getDdd();
        String numero = pessoa.getTelefones().get(0).getNumero();
        optional = pessoaRepository.findByTelefoneDddAndTelefoneNumero(ddd, numero);

        if (optional.isPresent()) {
            throw new UnicidadeTelefoneException(
                    "Já existe pessoa cadastrada com o telefone (" + pessoa.getTelefones().get(0).getDdd() + ")" + pessoa.getTelefones().get(0).getNumero());
        }

        return pessoaRepository.save(pessoa);
    }

    @Override
    public Pessoa buscarPorTelefone(Telefone telefone) throws TelefoneNaoEncontradoException {
        Optional<Pessoa> optional = pessoaRepository.findByTelefoneDddAndTelefoneNumero(telefone.getDdd(), telefone.getNumero());
        return optional.orElseThrow(() -> new TelefoneNaoEncontradoException("Não existe pessoa com o telefone (" + telefone.getDdd() + ")" + telefone.getNumero()));
    }
}
