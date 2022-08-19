package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ViaCepService cepService;

	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvaClienteComEndereco(cliente);

	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		// Procura o cliente pelo id informado
		Optional<Cliente> clienteAAtualizar = clienteRepository.findById(id);
		if (clienteAAtualizar.isPresent()) {
			salvaClienteComEndereco(cliente);
		}

	}

	@Override
	public void apagar(Long id) {
		clienteRepository.deleteById(id);

	}

	private void salvaClienteComEndereco(Cliente cliente) {
		// Verifica se o endereço do cliente já existe (pelo CEP)
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, interage com o ViaCEP e persiste o retorno
			Endereco novoEndereco = cepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir o cliente, vinculando o endereço (novo ou existente).
		clienteRepository.save(cliente);
	}

}
