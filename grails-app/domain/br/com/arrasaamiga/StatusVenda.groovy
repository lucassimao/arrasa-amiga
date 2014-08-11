package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.TransactionStatus

enum StatusVenda {

    AguardandoPagamento("Aguardando pagamento"),
    EmAnalise("Em análise"),
    PagamentoRecebido("Paga"),
    Disponivel("Disponível"),
    EmDisputa("Em disputa"),
    Devolvida("Devolvida"),
    Cancelada("Cancelada"),
    Entregue("Entregue")

    public static StatusVenda fromPagSeguroTransactionStatus(TransactionStatus status) {

        switch (status.value) {
            case 1:
                return StatusVenda.AguardandoPagamento
            case 2:
                return StatusVenda.EmAnalise
            case 3:
                return StatusVenda.PagamentoRecebido
            case 4:
                return StatusVenda.Disponivel
            case 5:
                return StatusVenda.EmDisputa
            case 6:
                return StatusVenda.Devolvida
            case 7:
                return StatusVenda.Cancelada
            default:
                throw new IllegalArgumentException("status esconhecido: ${status}")
        }

    }


    String descricao

    StatusVenda(String desc) {
        this.descricao = desc
    }

    public String toString() {
        return this.descricao
    }

}
