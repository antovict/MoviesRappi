# Movies
**By: Antovict Rafael Gonzalez Millan**


La prueba está diseñada para evaluar tu conocimiento y experiencia como desarrollador Android.
Por favor ten en cuenta lo siguiente: 
- Consumir el API de películas y series de la siguiente pagina: https://developers.themoviedb.org/4/getting-started/authorization
- Debe tener tres categorías de películas y/o series: Popular, Top Rated, Upcoming. 
- Cada película y/o series debe poder visualizar su detalle. 
- Debe funcionar tanto online como offline (cache). 
- Debe tener un buscador offline por categorías. 

**Valoraciones extras:**
1. Visualización de los videos en el detalle de cada ítem. 
2. Transiciones, Animaciones, UI/UX. 
3. Buscador Online. 
4. Pruebas Unitarias

**Una vez acabada la prueba describa en un readme brevemente:** 
1. Las capas de la aplicación (por ejemplo capa de persistencia, vistas, red, negocio, etc) y qué clases pertenecen a cual. 
2. La responsabilidad de cada clase creada. 

# Respuestas

**1.- ¿Las capas de la aplicación (por ejemplo capa de persistencia, vistas, red, negocio, etc) y qué clases pertenecen a cual?**

- **Capa de Persistencia o Datos:** Es aquella que permite mantener una informacion para ser consultada. Entre las clases del proyectos que puedan calificar aca son:
  -  Movies y Movie (Clase Modelo para almacenar las Peliculas y Series obtenidos desde el server)
  -  Videos y Video (Clase Modelo para almacenar los Videos obtenidos desde el server)
  -  SingletonCache (Clase Modelo para almacenar en cache las peliculas y los Videos)
  -  Api (Clase Modelo Estatica para almacenar las api_key y token de la apk)
  -  Urls (Clase Modelo Estatica para almacenar las rutas de conexion para obtener las respuestas del server)
  
- **Capa de Presentacion o vistas:** Estas por lo general con las que el usuario final termina viendo. como su nomre lo indica sin simples vistas. En este caso las clases correspondientes a esta capa serian: 
  -  DetalleActivity (Permite Mostrar los detales de una Movie selecionada)
  -  MainActivity (Permite Mostrar todas las categorias y los listados de las movies por categorias)
  -  YoutubeActivity (Permite Mostrar un video en youtube pasandole los datos de la movie)
  -  SplashActivity (Permite muestrar la presentación)
  
- **Capa de Negocio:**  Esta capa es la que permite la iteracion entre los datos y las vistas. Podria denominarse un controlador. Entre las clases tenemos:
  -  FragmentContent (Permite unir la vista .xml con el controlador .java)
  -  AdapterItemVideo (Encargada de pasar la informacion de los modelos videos a las vistas)
  -  AdapterItemMovie (Encargada de pasar la informacion de los modelos movies a las vistas)

- **Capa de Red:** Es la que te permite mantener conexion online. Entre ellas estan:
  -  AsyncTask **[son las que hacen las conexiones al server o obtienen la informacion]**
  -  DownloadJSON **[(Permite cargar todos los datos de peliculas y Series)]**


**Responda y escriba dentro del Readme con las siguientes preguntas:** 
1. En qué consiste el principio de responsabilidad única? Cuál es su propósito? 
2. Qué características tiene, según su opinión, un “buen” código o código limpio? 


# Respuestas

**1.- ¿En que consiste el principio de responsabilidad unica? ¿Cúal es su proposito?**

El principio de responsabilidad unica consiste en que una clase o un modulo se encarge de una funcion en esecifico, es decir que tenga una sola responsabilidad. El proposito de que las clases o los modulos solo tengan una responsabilidad es que el codigo sea mantenible, facil de testear y se adapte sin dañar otras partes del programa.

**2.- ¿Qué caracteristicas tiene un "Buen" codigo o un codigo limpio?**

En lo personal opino que un Codigo limpio debe tener las siguientes caracteristicas:

- Los metodos deben encargarse de tareas en especifico, sin depender de otros modulos.
- Se debe evitar crear codigo repetido, se debe tratar de reutilizar el codigo lo mas posible.
- El nombre de las variables y metodos debe ser descriptivo para que el codigo sea facil de leer.
- Evitar codigos muy largos, mantenerlo simple crear modulos simples.
