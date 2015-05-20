package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.AccountCredentials
import br.com.uol.pagseguro.domain.Transaction
import br.com.uol.pagseguro.domain.checkout.Checkout
import br.com.uol.pagseguro.enums.Currency
import br.com.uol.pagseguro.enums.PaymentMethodCode
import br.com.uol.pagseguro.enums.ShippingType
import br.com.uol.pagseguro.enums.TransactionStatus
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import br.com.uol.pagseguro.service.NotificationService
import br.com.uol.pagseguro.service.TransactionSearchService

import java.text.NumberFormat

class PagSeguroService {

    boolean transactional = true

    def getAccountCredentials() {
        String token = '9A9DE1A43A2045DEBBD66D629FC4F76B'
        def accountCredentials = new AccountCredentials('lsimaocosta@gmail.com', token)
        return accountCredentials
    }

    public Transaction getTransaction(String transacaoPagSeguro) {

        try {
            return TransactionSearchService.searchByCode(getAccountCredentials(), transacaoPagSeguro)
        } catch (PagSeguroServiceException e) {
            e.printStackTrace()
            return null
        }
    }

    public StatusVenda getStatusTransacao(String transacaoPagSeguro) {
        Transaction transaction = getTransaction(transacaoPagSeguro)
        return getStatusTransacao(transaction.status)
    }

    public StatusVenda getStatusTransacao(TransactionStatus status) {

        switch (status) {
            case TransactionStatus.WAITING_PAYMENT:
                return StatusVenda.AguardandoPagamento
            case TransactionStatus.IN_ANALYSIS:
                return StatusVenda.EmAnalise
            case TransactionStatus.PAID:
                return StatusVenda.PagamentoRecebido
            case TransactionStatus.AVAILABLE:
                return StatusVenda.Disponivel
            case TransactionStatus.IN_DISPUTE:
                return StatusVenda.EmDisputa
            case TransactionStatus.REFUNDED:
                return StatusVenda.Devolvida
            case TransactionStatus.CANCELLED:
                return StatusVenda.Cancelada
            default:
                throw new IllegalArgumentException("status esconhecido: ${status}")
        }

    }


    public String getDetalhesPagamento(String transacaoPagSeguro) {
        Transaction transaction = getTransaction(transacaoPagSeguro)
        if (transaction)
            return getDetalhesPagamento(transaction)
        else
            return 'Indisponível'
    }


    public String getDetalhesPagamento(Transaction transaction) {
        int numParcelas = transaction.getInstallmentCount()
        PaymentMethodCode paymentMethodCode = transaction.getPaymentMethod().getCode()

        switch (paymentMethodCode.value) {
            case 101: return "Cartão de crédito VISA em ${numParcelas}X"
            case 102: return "Cartão de crédito MasterCard em ${numParcelas}X"
            case 103: return "Cartão de crédito American Express em ${numParcelas}X"
            case 104: return "Cartão de crédito Diners em ${numParcelas}X"
            case 105: return "Cartão de crédito Hipercard em ${numParcelas}X"
            case 106: return "Cartão de crédito Aura em ${numParcelas}X"
            case 107: return "Cartão de crédito Elo em ${numParcelas}X"
            case 108: return "Cartão de crédito PLENOCard em ${numParcelas}X"
            case 109: return "Cartão de crédito PersonalCard em ${numParcelas}X"
            case 110: return "Cartão de crédito JCB em ${numParcelas}X"
            case 111: return "Cartão de crédito Discover em ${numParcelas}X"
            case 112: return "Cartão de crédito BrasilCard em ${numParcelas}X"
            case 113: return "Cartão de crédito FORTBRASIL em ${numParcelas}X"
            case 114: return "Cartão de crédito CARDBAN em ${numParcelas}X"
            case 115: return "Cartão de crédito VALECARD em ${numParcelas}X"
            case 116: return "Cartão de crédito Cabal em ${numParcelas}X"
            case 117: return "Cartão de crédito Mais! em ${numParcelas}X"
            case 118: return "Cartão de crédito Avista em ${numParcelas}X"
            case 119: return "Cartão de crédito GRANDCARD em ${numParcelas}X"
            case 201: return "Boleto Bradesco"
            case 202: return "Boleto Santander"
            case 301: return "Transferência eletrônica de fundos do banco Bradesco"
            case 302: return "Transferência eletrônica de fundos do banco Itaú"
            case 303: return "Transferência eletrônica de fundos do Unibanco"
            case 304: return "Transferência eletrônica de fundos do Banco do Brasil"
            case 305: return "Transferência eletrônica de fundos do banco Real"
            case 306: return "Transferência eletrônica de fundos do banco Banrisul"
            case 401: return "Saldo de conta PagSeguro"
            case 501: return "Oi Paggo"
            case 701: return "Depósito em conta - Banco do Brasil"
            default:
                return "Pagamento codigo ${paymentMethodCode.value} ${paymentMethodCode}"
        }

    }

    public Transaction checkTransaction(String notificationCode) {
        return NotificationService.checkTransaction(getAccountCredentials(), notificationCode)
    }

    public String getPaymentURL(Venda venda) {

        if (!venda.id)
            throw new IllegalStateException("A venda deve estar salva!")

        if (venda.formaPagamento == FormaPagamento.AVista) {
            throw new IllegalStateException("Vendas a vista não devem requisitar URL de pagamento")
        }

        def formatter = NumberFormat.getInstance(Locale.US)
        formatter.setMinimumFractionDigits(2)

        def paymentRequest = new Checkout()

        paymentRequest.setCurrency(Currency.BRL)

        // especificando os itens
        venda.itensVenda.each { item ->
            Double valorUnitario = item.precoAPrazoEmReais

            paymentRequest.addItem(item.id.toString(), item.produto.nome + "-" + item.unidade, item.quantidade,
                    new BigDecimal(formatter.format(valorUnitario)), null, null)
        }

        // nome completo, email, DDD e número de telefone
        Cliente cliente = venda.cliente
        paymentRequest.setSender(cliente.nome, cliente.email, cliente.dddTelefone, cliente.telefone)
        // país, estado, cidade, bairro, CEP, rua, número, complemento
        Endereco endereco = cliente.endereco
        String complemento =  endereco.complemento
        if (complemento?.length() > 60)
            complemento = complemento[0..<60]

        paymentRequest.setShippingAddress("BRA", endereco.uf.sigla, endereco.cidade.nome, endereco.bairro,
                endereco.cep, complemento, "0", '')

        paymentRequest.shippingCost = new BigDecimal(formatter.format(venda.freteEmReais))
        paymentRequest.setReference(venda.id.toString())
        paymentRequest.redirectURL = "http://www.arrasaamiga.com.br/pagSeguro/retorno/${venda.id}"
        paymentRequest.notificationURL = "http://www.arrasaamiga.com.br/pagSeguro/notificacoes/${venda.id}"

        if (venda.cliente.isDentroDaAreaDeEntregaRapida())
            paymentRequest.shippingType = ShippingType.NOT_SPECIFIED
        else
            paymentRequest.shippingType = (venda.servicoCorreio == ServicoCorreio.SEDEX) ? ShippingType.SEDEX : ShippingType.PAC

        return paymentRequest.register(getAccountCredentials())
    }

}
