
import java.util.Scanner;

public class PostoPatos {

    static Scanner scanner = new Scanner(System.in);

    static double[] precoDinheiro = {6.00, 5.50, 5.30, 4.00};
    static double[] precoCartao = {6.20, 5.70, 5.50, 4.20};
    static double[] estoque = {100, 100, 100, 100};

    static Cliente[] clientes = new Cliente[100];
    static int numClientes = 0;
    static double totalVendido = 0;
    static double[] litrosVendidos = {0, 0, 0, 0};

    public static void main(String[] args) {
        while (true) {
            if (realizarLogin() == 1) {
                System.out.print("Nome do Cliente: ");
                String nomeCliente = scanner.next();
                Cliente cliente = encontrarCliente(nomeCliente);
                if (cliente == null) {
                    cliente = new Cliente(nomeCliente);
                    clientes[numClientes++] = cliente;
                }

                while (true) {
                    mostrarMenu();
                    if (novaVenda(cliente) == 2) {
                        System.out.print("Deseja fazer login com um novo cliente? 1-S e 2-N ");
                        int continuar = scanner.nextInt();
                        if (continuar == 1) {
                            break;
                        } else {
                            System.out.print("Digite o nome do cliente: ");
                            nomeCliente = scanner.next();
                            cliente = encontrarCliente(nomeCliente);
                            if (cliente == null) {
                                cliente = new Cliente(nomeCliente);
                                clientes[numClientes++] = cliente;
                            }
                        }
                    }
                }
            } else {
                System.out.println("Login ou senha incorretos.");
            }
        }
    }

    public static int realizarLogin() {
        System.out.print("Login: ");
        int login = scanner.nextInt();
        System.out.print("Senha: ");
        int senha = scanner.nextInt();
        if (login == 111 && senha == 111) {
            return 1;
        } else {
            return 2;
        }
    }

    public static void mostrarMenu() {
        System.out.println("### Posto Patos ###");
        System.out.println("1- Gasolina Comum");
        System.out.println("2- Gasolina Aditivada");
        System.out.println("3- Diesel");
        System.out.println("4- Etanol");
    }

    public static int novaVenda(Cliente cliente) {
        System.out.println("Cliente " + cliente.nome);

        System.out.print("Tipo do Combustível? ");
        int tipoCombustivel = scanner.nextInt() - 1;

        System.out.print("Valor desejado? ");
        double valor = scanner.nextDouble();

        System.out.print("Forma de pagamento? (1-Dinheiro/Débito/Pix, 2-Cartão) ");
        int formaPagamento = scanner.nextInt();

        double preco = (formaPagamento == 1) ? precoDinheiro[tipoCombustivel] : precoCartao[tipoCombustivel];
        double litros = valor / preco;

        if (estoque[tipoCombustivel] < litros) {
            System.out.println("Combustível insuficiente. Estoque atual: " + estoque[tipoCombustivel] + " litros.");
            return 1;
        }

        estoque[tipoCombustivel] -= litros;
        litrosVendidos[tipoCombustivel] += litros;
        totalVendido += valor;

        cliente.litrosComprados[tipoCombustivel] += litros;
        cliente.totalGasto += valor;

        System.out.println("Nota Fiscal");
        System.out.println("Cliente " + cliente.nome);
        System.out.println("Tipo de Combustível: " + getNomeCombustivel(tipoCombustivel));
        System.out.println("Valor pago: R$ " + valor);
        System.out.println("Litros comprados: " + String.format("%.2f", litros));

        System.out.print("Deseja continuar comprando? 1-S e 2-N ");
        int continuar = scanner.nextInt();
        return continuar;
    }

    public static String getNomeCombustivel(int tipo) {
        switch (tipo) {
            case 0:
                return "Gasolina Comum";
            case 1:
                return "Gasolina Aditivada";
            case 2:
                return "Diesel";
            case 3:
                return "Etanol";
            default:
                return "Desconhecido";
        }
    }

    public static Cliente encontrarCliente(String nomeCliente) {
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].nome.equals(nomeCliente)) {
                return clientes[i];
            }
        }
        return null;
    }

    public static void imprimirRelatorio() {
        System.out.println("### Relatório Final ###");
        System.out.println("Total faturado: R$ " + totalVendido);
        for (int i = 0; i < estoque.length; i++) {
            System.out.println(getNomeCombustivel(i) + " - Litros vendidos: " + String.format("%.2f", litrosVendidos[i]) + " - Estoque restante: " + String.format("%.2f", estoque[i]));
        }

        for (int i = 0; i < numClientes; i++) {
            Cliente cliente = clientes[i];
            System.out.println("Cliente: " + cliente.nome);
            System.out.println("Total gasto: R$ " + cliente.totalGasto);
            for (int j = 0; j < estoque.length; j++) {
                System.out.println(getNomeCombustivel(j) + " - Litros comprados: " + String.format("%.2f", cliente.litrosComprados[j]));
            }
        }
    }
}

class Cliente {
    String nome;
    double[] litrosComprados = new double[4];
    double totalGasto = 0;

    public Cliente(String nome) {
        this.nome = nome;
    }
}
