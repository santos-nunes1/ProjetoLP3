var meuMapa;

function init() {
    meuMapa = new ol.Map({
        target: MeuMapa,
        renderer: 'canvas',
        view: new ol.View({
            projection: 'EPSG:900913',
            center: [0, 0],
            zoom: 3
        })
    });
    var openStreetMapLayer = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
    meuMapa.addLayer(openStreetMapLayer);
    var bingLayer = new ol.layer.Tile({
        source: new ol.source.BingMaps({
            imagerySet: 'Aerial',
            key: 'Ak-dzM4wZjSqTlzveKz5u0d4IQ4bRzVI309GxmkgSVr1ewS6iPSrOvOKhACJlm3'
        })
    });
    bingLayer.setOpacity(.3);
    meuMapa.addLayer(bingLayer);
    var pointStyle = new ol.style.Style({
        image: new ol.style.Icon(({
            anchor: [0, 0],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.75,
            src: 'js/dados/r2d2.png'
        }))
    });


// carrega na variável userName o valor do campo escondido com id="userValue"

    var userName = location.search.split('userName=')[1];;

//apresenta no console de execução do javascript o valor da variável userName

    console.log(userName);

//define a url da chamada REST (GET)

    var urlString = 'http://localhost:8080/AppFrontController/LP3Rest/lp3/posicoesJSON/';

//concatena o nome do usuário à consulta REST

    urlString = urlString.concat(userName);

//executa a chamada ao webservice REST, recebe a resposta em um vetor de objetos JSON chamado data

//chama a função plotOnMap passando o vetor como parametro.

    $.ajax({
        url: urlString,
        data: {
            format: 'xml'
        },
        success: function (data) {
            console.log(data);
            plotOnMap(data);
        },
        error: function (e) {
            console.log(e.message);
        },
        type: 'GET'
    });

//Define um estilo para os pontos a serem plotados.

//Indica o icone que desejamos utilizar para representar as posições

    var pointStyle = new ol.style.Style({
        image: new ol.style.Icon(({
            anchor: [0, 0],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.75,
            src: 'js/dados/r2d2.png'
        }))
    });

//Function que recebe a lista de objetos e plota os pontos

    function plotOnMap(json) {

        // cria arranjo de features
        var features = new Array();

        console.log(json.length);
        var count = 0;
        for (var i = 0; i < json.length; i++) {

            // cria ponto a partir de coordenadas em graus
            var point = [parseFloat(json[i].lon), parseFloat(json[i].lat)];
            
            // converte para coordenadas de tela
            var pos = ol.proj.fromLonLat(point);

            // cria feature

            var feature = new ol.Feature(new ol.geom.Point(pos));

            // define o estilo da feature
            feature.setStyle(pointStyle);

            //adiciona feature a lista de features

            features[features.length] = feature;
        }

        // cria um vetor de features para colocação em uma camada de visualização 
        var source = new ol.source.Vector({
            features: features
        });

        //cria uma camada com o vetor de features
        featuresLayer = new ol.layer.Vector({
            source: source,
            style: pointStyle
        });

        //adiciona a camada ao mapa

        meuMapa.addLayer(featuresLayer);
        var element = document.getElementById('popup');
        var popup = new ol.Overlay({
            element: element,
            positioning: 'bottom-center',
            stopEvent: false
        });
        meuMapa.addOverlay(popup);
        meuMapa.on('click', function (evt) {
            var feature = meuMapa.forEachFeatureAtPixel(evt.pixel,
                    function (feature, layer) {
                        return feature;
                    });
            if (feature) {
                popup.setPosition(evt.coordinate);
                var xmlString;
                
                var urlString = 'http://localhost:8080/AppFrontController/LP3Rest/lp3/posicoes/';
                console.log(urlString);
                urlString = urlString.concat(userName);
                console.log(urlString);
                $.ajax({
                    url: urlString,
                    data: {
                        format: 'xml'
                    },
                    success: function (data) {
                        xmlString = (new XMLSerializer()).serializeToString(data);
                        console.log(xmlString);
                        $(element).popover({
                            'placement': 'top',
                            'html': true,
                            'content': '<p>' + xmlString + '</p>'
                        });
                        $(element).popover('show');
                    },
                    error: function (e) {
                        console.log(e.message);
                    },
                    type: 'GET'
                });
            } else {
                $(element).popover('destroy');
            }
        });
        meuMapa.on('pointermove', function (e) {
        if (e.dragging) {
            $(element).popover('destroy');
            return;
        }
        var pixel = meuMapa.getEventPixel(e.originalEvent);
        var hit = meuMapa.hasFeatureAtPixel(pixel);
    });
    }
}
