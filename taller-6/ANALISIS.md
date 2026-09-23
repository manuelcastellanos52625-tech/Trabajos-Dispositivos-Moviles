Analisis taller 5

Prediccion inicial

Mi prediccion era que si toco la misma tarjeta 3 veces rapido navega 3 veces y quedan 3 detalles apilados,
porque cada navigate() agrega una pantalla nueva a la pila sin fijarse si ya esta. Entonces para volver a la
lista tocaria darle 3 veces a Volver. Tenia la duda de si el tercer toque alcanzaba a llegar antes de que
cambiara la pantalla, asi que tambien podian quedar solo 2.

Al final si quedaron 3.


Parte 3 - navegacion duplicada

Lo que paso: toque la tarjeta de San Andres 3 veces muy rapido y se abrio el detalle normal (bug_antes_1.png),
pero cuando le di a Volver me volvio a salir San Andres (bug_antes_2.png), le di otra vez y otra vez San Andres
(bug_antes_3.png), y solo a la tercera volvi a la lista (bug_antes_4.png). O sea habian 3 detalles iguales
apilados.

Por que pasa: navigate() siempre pone la pantalla encima de la pila, no revisa si arriba ya esta ese mismo
detalle. Y el cambio de pantalla tiene una animacion, mientras dura la lista todavia esta ahi y se puede seguir
tocando, entonces cada toque vuelve a llamar navigate() y apila otro detalle. popBackStack() solo quita una
pantalla, por eso toca darle Volver 3 veces. Si toco despacio no pasa porque cuando termina la animacion la
lista ya no esta.

Como lo corregi: con launchSingleTop en el navigate

    navController.navigate("detalle/$id") {
        launchSingleTop = true
    }

Con eso si el detalle ya esta arriba no lo vuelve a apilar. Hice lo mismo de tocar 3 veces rapido y ahora con
un solo Volver regreso a la lista (bug_despues_1.png y bug_despues_2.png).


Caso limite con dos tarjetas distintas

Toque San Andres y enseguida Parque Tayrona.

Sin launchSingleTop se apilaron los dos, se veia Tayrona, al darle Volver aparecia San Andres y otra vez Volver
para llegar a la lista (dos_tarjetas_antes_1, 2 y 3).

Con launchSingleTop pense que iba a pasar lo mismo porque son destinos distintos, pero no: quedo solo Tayrona y
con un Volver ya estaba en la lista (dos_tarjetas_despues_1 y 2). Lo que pasa es que launchSingleTop compara la
ruta "detalle/{elementoId}" y no el id, entonces para la navegacion detalle/2 y detalle/3 son la misma pantalla
y reemplaza la de arriba en vez de poner otra.

Diferencia entre mismo destino repetido y destinos distintos en cadena:
- mismo destino repetido (detalle/2 y otra vez detalle/2) es el bug, son dos pantallas iguales y lo unico que
  hace es que Volver parezca que no sirve. Eso es lo que arregla launchSingleTop.
- destinos distintos en cadena (detalle/2 y despues detalle/3) si puede ser navegacion normal, por ejemplo si
  desde un detalle se pudiera ir a otro destino, al volver uno esperaria ver el anterior.

En esta app esta bien que se reemplace porque a un detalle solo se llega desde la lista, entonces dos detalles
seguidos solo pasan por tocar dos tarjetas sin querer. Pero si la app tuviera navegacion de detalle a detalle
no convendria usar launchSingleTop ahi porque se perderia la pantalla anterior.


Otras cosas

- Con el codigo del paso 1.4 la busqueda se borraba al volver del detalle (busqueda_se_borraba.png), porque con
  remember el valor se pierde cuando la lista sale de pantalla al navegar. Lo cambie a rememberSaveable y ya se
  conserva (busqueda_arreglada.png), tanto con el boton Volver como con el boton atras del celular.
- Use firstOrNull en vez de first para que no se cierre la app si llega un id que no existe, y no use !!.
- Hice el recorrido lista -> detalle -> volver -> buscar -> detalle -> volver 5 veces seguidas y funciono bien.
