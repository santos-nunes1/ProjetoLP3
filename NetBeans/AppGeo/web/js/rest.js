var meuMapa;
function init() {
    meuMapa = new ol.Map({
        target: 'MeuMapa',
        renderer: 'canvas',
        view: new ol.View({
            projection: 'EPSG:900913',
            center: [-5193252.61684, -2698365.38923],
            zoom: 18
        })
    });
    var openStreetMapLayer = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
    meuMapa.addLayer(openStreetMapLayer);
    var iconFeature = new ol.Feature({
        geometry: new ol.geom.Point([-5193252.61684, -2698365.38923]),
        name: 'mackTest@mack.br'
    });
    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon(({
            anchor: [0.5, 46],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            opacity: 0.75,
            src: 'js/dados/r2d2.png'
        }))
    });
    iconFeature.setStyle(iconStyle);
    var vectorSource = new ol.source.Vector({
        features: [iconFeature]
    });
    var vectorLayer = new ol.layer.Vector({
        source: vectorSource
    });
    meuMapa.addLayer(vectorLayer);
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
            var login = feature.get('name')
            var urlString = 'http://localhost:8080/AppFrontController/LP3Rest/lp3/posicoes/';
            console.log(urlString);
            urlString = urlString.concat(login);
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