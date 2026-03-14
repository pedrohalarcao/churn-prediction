document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('churn-prediction-form');
    const predictionResultDiv = document.getElementById('prediction-result');

    form.addEventListener('submit', async (event) => {
        event.preventDefault(); // Impedir o envio de formulário padrão

        predictionResultDiv.innerHTML = 'Previsão em andamento...';
        predictionResultDiv.style.backgroundColor = '#e0f7fa';
        predictionResultDiv.style.color = '#0056b3';

        const formData = new FormData(form);
        const raw = {};
        for (let [key, value] of formData.entries()) {
            raw[key] = value;
        }

        // Mapear os campos do front (snake_case / opções) para o DTO esperado pelo backend
        const data = {
            gender: raw.gender === '1' ? 'MALE' : 'FEMALE',
            seniorCitizen: raw.senior_citizen === '1',
            partner: raw.partner === '1',
            dependents: raw.dependents === '1',
            contractMonths: parseInt(raw.contract_months, 10),
            phoneService: raw.phone_service === '1',
            multipleLines: (raw.multiple_lines || '').toUpperCase(),
            internetService: (raw.internet_service || '').toUpperCase(),
            onlineSecurity: (raw.online_security || '').toUpperCase(),
            onlineBackup: (raw.online_backup || '').toUpperCase(),
            deviceProtection: (raw.device_protection || '').toUpperCase(),
            techSupport: (raw.tech_support || '').toUpperCase(),
            streamingTV: (raw.streaming_tv || '').toUpperCase(),
            streamingMovies: (raw.streaming_movies || '').toUpperCase(),
            contractType: (raw.contract || '').toUpperCase(),
            paperlessBilling: raw.paperless_billing === '1',
            paymentMethod: (raw.payment_method || '').toUpperCase(),
            monthlyCharges: parseFloat(raw.monthly_charges),
            totalCharges: parseFloat(raw.total_charges)
        };

        // Ajustes de valores para combinar com os enums do backend
        // multipleLines: front usa 'yes'/'no'/'no_phone_service' -> backend espera YES/NO/NO_PHONE_SERVICE
        if (data.multipleLines) {
            data.multipleLines = data.multipleLines.replace(/ /g, '_').toUpperCase();
        }
        // internetService: 'dsl'|'fiber_optic'|'no' -> backend uses DSL/FIBER_OPTIC/NONE (NONE vs 'no')
        if (data.internetService === 'NO') data.internetService = 'NONE';
        if (data.internetService) data.internetService = data.internetService.replace(/ /g, '_').toUpperCase();

        // StateService enums (onlineSecurity, onlineBackup, etc.) expect YES/NO/NO_INTERNET_SERVICE
        const mapState = (v) => v === 'NO_INTERNET_SERVICE' ? 'NO_INTERNET_SERVICE' : (v === 'YES' ? 'YES' : 'NO');
        data.onlineSecurity = mapState(data.onlineSecurity);
        data.onlineBackup = mapState(data.onlineBackup);
        data.deviceProtection = mapState(data.deviceProtection);
        data.techSupport = mapState(data.techSupport);
        data.streamingTV = mapState(data.streamingTV);
        data.streamingMovies = mapState(data.streamingMovies);

        // contractType mapping: month_to_month -> MONTH_TO_MONTH etc.
        data.contractType = data.contractType.replace(/ /g, '_').toUpperCase();

        // paymentMethod mapping: electronic_check -> ELECTRONIC_CHECK etc.
        data.paymentMethod = data.paymentMethod.replace(/ /g, '_').toUpperCase();

        console.log('Dados enviados para a API:', data);

        // Validações cliente para respeitar as restrições do backend
        const clientError = (msg) => {
            predictionResultDiv.innerHTML = `Erro de validação: ${msg}`;
            predictionResultDiv.style.backgroundColor = '#ffe0e0';
            predictionResultDiv.style.color = '#cc0000';
        };

        if (isNaN(data.monthlyCharges) || data.monthlyCharges < 0) {
            clientError('monthlyCharges inválido (deve ser >= 0)');
            return;
        }
        if (data.monthlyCharges > 200) {
            clientError('monthlyCharges é muito alto (máx 200)');
            return;
        }
        if (isNaN(data.totalCharges) || data.totalCharges < 0) {
            clientError('totalCharges inválido (deve ser >= 0)');
            return;
        }
        if (data.totalCharges > 10000) {
            clientError('totalCharges é muito alto (máx 10000)');
            return;
        }
        if (isNaN(data.contractMonths) || data.contractMonths < 0 || data.contractMonths > 72) {
            clientError('contractMonths inválido (0-72)');
            return;
        }
        try {
        
            // Envia para o endpoint correto do backend
            const response = await fetch('http://localhost:8080/api/predict', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Erro na rede ou na API: ${response.status} - ${errorText}`);
            }

            const result = await response.json();
            console.log('Resposta da API:', result);

            // Backend returns { id, prediction: "Churn"|"No Churn", probability }
            const prediction = result.prediction;
            const probability = result.probability;

            let message = '';
            if (prediction && prediction.toLowerCase().includes('churn')) {
                message = `O cliente irá evadir com ${(probability * 100).toFixed(2)}% de probabilidade.`;
                predictionResultDiv.style.backgroundColor = '#ffe0e0';
                predictionResultDiv.style.color = '#cc0000';
            } else {
                message = `O cliente NÃO irá evadir com ${( (1 - probability) * 100).toFixed(2)}% de probabilidade.`;
                predictionResultDiv.style.backgroundColor = '#e0ffe0';
                predictionResultDiv.style.color = '#006600';
            }

            predictionResultDiv.innerHTML = message;

        } catch (error) {
            console.error('Erro ao fazer a previsão:', error);
            predictionResultDiv.innerHTML = `Erro ao fazer a previsão: ${error.message}`;
            predictionResultDiv.style.backgroundColor = '#ffe0e0';
            predictionResultDiv.style.color = '#cc0000';
        }
    });
});
