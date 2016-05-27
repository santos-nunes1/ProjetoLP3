var meuMapa;
function init() {
    meuMapa = new ol.Map({
        target: MeuMapa,
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
    var layergpx = new ol.layer.Vector({
        source: new ol.source.Vector({
            projection: 'EPSG:4326',
            format: new ol.format.GPX(),
            url: 'js/dados/posicao_proc.gpx'
        }),
        style: pointStyle
    });
    meuMapa.addLayer(layergpx);
    var displayFeatureInfo = function (pixel) {
        var features = [];
        meuMapa.forEachFeatureAtPixel(pixel, function (feature, layer) {
            features.push(feature);
        });
        if (features.length > 0) {
            var info = [];
            var i, ii;
            for (i = 0, ii = features.length; i < ii; ++i) {
                info.push(features[i].get('name'));
            }
            document.getElementById('info').innerHTML = info.join(', ') || '(unknown)';
            meuMapa.getTarget().style.cursor = 'pointer';
        } else {
            document.getElementById('info').innerHTML = '&nbsp;';
            meuMapa.getTarget().style.cursor = '';
        }
    };
    meuMapa.on('pointermove', function (evt) {
        if (evt.dragging) {
            return;
        }
        var pixel = map.getEventPixel(evt.originalEvent);
        displayFeatureInfo(pixel);
    });
    meuMapa.on('click', function (evt) {
        displayFeatureInfo(evt.pixel);
    });
}