package com.example.taller6

data class Elemento(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val descripcionCorta: String,
    val descripcionLarga: String
)

val elementos = listOf(
    Elemento(
        1, "Cartagena de Indias", "Ciudades",
        "Ciudad amurallada frente al mar Caribe, en Bolívar.",
        "Su puerto, fortalezas y conjunto monumental fueron declarados Patrimonio de la Humanidad por la " +
            "UNESCO en 1984. Se recorre a pie por el centro histórico dentro de las murallas, el barrio " +
            "Getsemaní y el Castillo de San Felipe de Barajas, la mayor fortificación española en América."
    ),
    Elemento(
        2, "San Andrés", "Playas",
        "La isla del \"mar de siete colores\".",
        "Isla del archipiélago de San Andrés, Providencia y Santa Catalina, en el Caribe colombiano. El " +
            "archipiélago hace parte de la Reserva de Biosfera Seaflower, declarada por la UNESCO en 2000. " +
            "Son famosos Johnny Cay, el Acuario y la vuelta a la isla por la carretera circunvalar."
    ),
    Elemento(
        3, "Parque Tayrona", "Playas",
        "Playas entre la selva y la Sierra Nevada, en Magdalena.",
        "El Parque Nacional Natural Tayrona está cerca de Santa Marta, donde la Sierra Nevada de Santa " +
            "Marta llega al mar. Se llega a playas como Arrecifes y Cabo San Juan del Guía por senderos entre " +
            "la selva. Guarda vestigios de la cultura tayrona, como Pueblito (Chairama)."
    ),
    Elemento(
        4, "Islas del Rosario", "Playas",
        "Archipiélago coralino cerca de Cartagena.",
        "Hace parte del Parque Nacional Natural Corales del Rosario y de San Bernardo, en Bolívar. Desde " +
            "Cartagena se llega en lancha. Tiene aguas cristalinas, arrecifes de coral y buenos lugares para " +
            "snorkel y buceo."
    ),
    Elemento(
        5, "Cabo de la Vela", "Playas",
        "Desierto y mar en la península de La Guajira.",
        "Corregimiento de Uribia, en La Guajira, habitado por el pueblo wayuu, para quien este es un lugar " +
            "sagrado (Jepira). Se destacan el Pilón de Azúcar, la playa Ojo de Agua y los atardeceres del " +
            "Faro. Es un destino muy conocido para practicar kitesurf."
    ),
    Elemento(
        6, "Barichara", "Pueblos",
        "Pueblo colonial de piedra y tapia pisada, en Santander.",
        "Es considerado uno de los pueblos más bonitos de Colombia y fue declarado Monumento Nacional en " +
            "1978. Tiene calles empedradas y casas blancas de tapia pisada. Desde aquí nace el Camino Real " +
            "hacia Guane, un sendero histórico de piedra."
    ),
    Elemento(
        7, "Villa de Leyva", "Pueblos",
        "Pueblo colonial de Boyacá con una enorme plaza empedrada.",
        "Fue fundado en 1572 y declarado Monumento Nacional en 1954. Su Plaza Mayor empedrada es una de las " +
            "más grandes de Colombia. Cerca están el Fósil (un kronosaurio), el Pozo de la Vieja y el " +
            "Infiernito, un observatorio astronómico muisca."
    ),
    Elemento(
        8, "Guatapé", "Pueblos",
        "El pueblo de los zócalos de colores, en Antioquia.",
        "Las fachadas de sus casas están decoradas con zócalos, relieves de colores que cuentan historias. " +
            "Queda junto al embalse Peñol-Guatapé y a la Piedra del Peñol, un monolito de más de 200 metros " +
            "que se sube por una escalera de más de 700 escalones."
    ),
    Elemento(
        9, "Salento", "Pueblos",
        "Pueblo cafetero del Quindío, puerta al Valle de Cocora.",
        "Hace parte del Paisaje Cultural Cafetero, declarado Patrimonio de la Humanidad por la UNESCO en " +
            "2011. Tiene casas de bahareque con balcones de colores, la Calle Real y el Mirador Alto de la " +
            "Cruz. En sus alrededores se visitan fincas cafeteras."
    ),
    Elemento(
        10, "Santa Cruz de Mompox", "Pueblos",
        "Pueblo colonial a orillas del río Magdalena, en Bolívar.",
        "Fue fundado en 1537 y su centro histórico fue declarado Patrimonio de la Humanidad por la UNESCO " +
            "en 1995. Es famoso por su orfebrería en filigrana y por sus procesiones de Semana Santa. Tiene " +
            "iglesias como la de Santa Bárbara y casas coloniales frente al río."
    ),
    Elemento(
        11, "Caño Cristales", "Naturaleza",
        "El \"río de los cinco colores\", en la Sierra de la Macarena.",
        "Está en el departamento del Meta, dentro del Parque Nacional Natural Sierra de la Macarena. Sus " +
            "colores rojos se deben a la planta acuática Macarenia clavigera. Se visita en temporada, " +
            "aproximadamente entre junio y noviembre, cuando la planta está en su mejor color."
    ),
    Elemento(
        12, "Valle de Cocora", "Naturaleza",
        "El hogar de la palma de cera, el árbol nacional.",
        "Está en Salento, Quindío. Es el mejor lugar para ver la palma de cera del Quindío (Ceroxylon " +
            "quindiuense), árbol nacional de Colombia, que puede superar los 50 metros de altura. Tiene " +
            "caminatas por bosque de niebla hasta la reserva Acaime."
    ),
    Elemento(
        13, "Desierto de la Tatacoa", "Naturaleza",
        "Paisaje árido de cárcavas rojas y grises, en el Huila.",
        "Está en Villavieja, Huila. Aunque se le llama desierto, es un bosque seco tropical. Tiene dos zonas " +
            "de colores: el Cusco (ocre y rojo) y Los Hoyos (gris). Su cielo despejado lo hace ideal para " +
            "observar estrellas desde su observatorio astronómico."
    ),
    Elemento(
        14, "Medellín", "Ciudades",
        "La \"ciudad de la eterna primavera\", en Antioquia.",
        "Está en el Valle de Aburrá. Es conocida por el Metrocable, cables aéreos integrados al Metro, y por " +
            "la transformación de la Comuna 13 y sus escaleras eléctricas y grafitis. En agosto celebra la " +
            "Feria de las Flores con el Desfile de Silleteros."
    ),
    Elemento(
        15, "Bogotá", "Ciudades",
        "La capital, a unos 2.600 metros sobre el nivel del mar.",
        "En su centro histórico, La Candelaria, están el Museo del Oro, con una de las colecciones de " +
            "orfebrería prehispánica más grandes del mundo, y el Museo Botero. Desde el cerro de Monserrate, " +
            "a unos 3.150 metros, se ve toda la ciudad."
    )
)

fun iconoDeCategoria(categoria: String): Int = when (categoria) {
    "Ciudades" -> R.drawable.ic_ciudad
    "Playas" -> R.drawable.ic_playa
    "Pueblos" -> R.drawable.ic_pueblo
    else -> R.drawable.ic_naturaleza
}
