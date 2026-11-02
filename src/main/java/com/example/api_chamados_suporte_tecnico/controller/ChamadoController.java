package com.example.api_chamados_suporte_tecnico.controller;

import com.example.api_chamados_suporte_tecnico.model.Chamado;
import com.example.api_chamados_suporte_tecnico.model.Chamado.Prioridade;
import com.example.api_chamados_suporte_tecnico.model.Chamado.Status;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    public record ResumoDTO(long abertos, long emAndamento, long resolvidos, long total) {}

    private long contadorId = 10L;

    private final List<Chamado> chamados = new ArrayList<>(
        List.of(
            new Chamado(1L, "Impressora não liga", "Usuario 1", "Financeiro", Prioridade.MEDIA, Status.ABERTO),
            new Chamado(2L, "Queda de internet no setor", "Usuario 2", "Operações", Prioridade.ALTA, Status.EM_ANDAMENTO),
            new Chamado(3L, "Sem acesso ao sistema de pagamentos", "Usuario 3", "Financeiro", Prioridade.ALTA, Status.ABERTO),
            new Chamado(4L, "Substituição de teclado", "Usuario 4", "RH", Prioridade.BAIXA, Status.RESOLVIDO),
            new Chamado(5L, "Monitor piscando", "Usuario 5", "Marketing", Prioridade.MEDIA, Status.EM_ANDAMENTO),
            new Chamado(6L, "Erro no servidor de arquivos", "Usuario 6", "TI", Prioridade.ALTA, Status.ABERTO),
            new Chamado(7L, "Configuração de e-mail corporativo", "Usuario 7", "Comercial", Prioridade.BAIXA, Status.RESOLVIDO),
            new Chamado(8L, "Lentidão ao abrir planilhas", "Usuario 8", "Financeiro", Prioridade.BAIXA, Status.ABERTO),
            new Chamado(9L, "Impressora travando papel", "Usuario 9", "RH", Prioridade.MEDIA, Status.RESOLVIDO),
            new Chamado(10L, "Instalação do pacote Office", "Usuario 10", "Comercial", Prioridade.BAIXA, Status.EM_ANDAMENTO)
        )
    );

    // Listar todos os chamados
    @GetMapping
    public List<Chamado> listarChamados() {
        return chamados;
    }

    // Listar chamados de alta prioridade ainda não resolvidos
    @GetMapping("/criticos")
    public List<Chamado> listarChamadosCriticos() {
        return chamados.stream()
                .filter(chamado -> chamado.getPrioridade() == Prioridade.ALTA && chamado.getStatus() != Status.RESOLVIDO)
                .toList();
    }

    // Consultar chamado por ID
    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscarChamadoPorId(@PathVariable Long id) {
        return chamados.stream()
                .filter(chamado -> chamado.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Filtrar por setor
    @GetMapping("/setor")
    public List<Chamado> filtrarChamadoPorSetor(@RequestParam String nome) {
        return chamados.stream()
                .filter(chamado -> chamado.getSetor().equalsIgnoreCase(nome))
                .toList();
    }

    // Filtrar por prioridade
    @GetMapping("/prioridade")
    public List<Chamado> filtrarChamadoPorPrioridade(@RequestParam String nivel) {
        return chamados.stream()
                .filter(chamado -> chamado.getPrioridade().name().equalsIgnoreCase(nivel))
                .toList();
    }

    // Buscar pelo título
    @GetMapping("/busca")
    public List<Chamado> buscarChamadoPorTitulo(@RequestParam String texto) {
        return chamados.stream()
                .filter(chamado -> chamado.getTitulo().toLowerCase().contains(texto.toLowerCase()))
                .toList();
    }

    // Retornar a quantidade de chamados por status
    @GetMapping("/resumo")
    public ResumoDTO obterQuantidadeChamadosStatus() {
        long abertos = chamados.stream().filter(chamado -> chamado.getStatus() == Status.ABERTO).count();
        long emAndamento = chamados.stream().filter(chamado -> chamado.getStatus() == Status.EM_ANDAMENTO).count();
        long resolvidos = chamados.stream().filter(chamado -> chamado.getStatus() == Status.RESOLVIDO).count();
        long total = chamados.size();

        return new ResumoDTO(abertos, emAndamento, resolvidos, total);
    }

    // Cadastrar um novo chamado (HttpStatus.CREATED = HTTP 201)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Chamado cadastrarChamado(@RequestBody Chamado chamado) {
        chamado.setId(++contadorId);

        if (chamado.getStatus() == null) {
            chamado.setStatus(Status.ABERTO);
        }

        chamados.add(chamado);

        return chamado;
    }
}