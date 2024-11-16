package com.picpay.domain.AuthorizeResponse;

public class AuthorizeResponse {
    private String status;
    private Data data;

    // Classe interna para encapsular os dados de autorização
    public static class Data {
        private boolean authorization;

        // Getter para a propriedade authorization
        public boolean isAuthorization() {
            return authorization;
        }

        // Setter para a propriedade authorization
        public void setAuthorization(boolean authorization) {
            this.authorization = authorization;
        }
    }

    // Getter e Setter para a propriedade status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter e Setter para a propriedade data
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
