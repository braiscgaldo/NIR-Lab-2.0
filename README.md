# NIR Lab Project

Continuación del trabajo fin de grado. Proyecto de desarrollo en investigación.

## Antes de comenzar...

Compilar la versión 1.0 de la aplicación e instalarla con el objetivo de comprobar que todo se encuentra correctamente.

Ajuste de los layouts del sistema para hacer uso de Constraints en lugar de Relative layout con el objetivo de mejorar la visualización y de la aplicación.

## Descripción del proyecto

Cada material posee una composición molecular específica que lo distingue del resto. Una forma de medir esta composición es a través de un espectrofotómetro [1]. Este dispositivo es capaz de analizar las diferentes estructuras moleculares en búsqueda de enlaces de carbono para tratar de identificar sus propiedades principales. La gran ventaja sobre la espectrometría de masas [2] es que esta es una técnica no destructiva, es decir, no destruye la muestra a la hora de analizarla. Esto es una gran ventaja puesto que permite realizar todas las mediciones que sean necesarias sobre una muestra [3].

Estos dispositivos han ido evolucionando en precisión, complejidad y tamaño, llegando a alcanzar bandas muy bajas (en torno a los 900 nm) y con menor tamaño. Si bien, para realizar un análisis muy complejo es necesario un espectrofotómetro de gran tamaño y gran complejidad con el fin de obtener unas mediciones lo más precisas posibles. Sin embargo, en un análisis más liviano es suficiente con el uso de un dispositivo de menor tamaño. Estos últimos también pueden ser utilizados para realizar mediciones a pie de campo, cuando no existe la posibilidad de transportar la muestra hasta el laboratorio. A lo largo del desarrollo del Trabajo Fin de Grado [4] se ha desarrollado una aplicación móvil Android capaz de conectarse con uno de estos dispositivos para tomar mediciones a pie de campo haciendo uso de la tecnología Bluetooth Low Energy. También se ha llevado a cabo la implementación de una forma de almacenar las muestras dentro de un archivo en formato JSON, y se han implementado únicamente el uso de modelos basados en TensorFlow Lite [7] previamente entrenados para la predicción de características. Esta última funcionalidad se ha complementado con una tarea de investigación para comprobar el correcto funcionamiento del sistema desarrollado.

Entre las principales funcionalidades implementadas en el TFG es la comunicación con el espectrofotómetro mediante la tecnología Bluetooth Low Energy. De esta manera, se permite la realización de mediciones así como modificar la configuración del dispositivo. Las mediciones así recogidas pueden ser utilizadas con uno de los modelos mencionados en en párrafo anterior con el objetivo de realizar predicciones. Una de las limitaciones que presenta en la actualidad el sistema es que no permite el desarrollo de los modelos dentro del mismo ni tampoco tiene ningún sistema asociado para esta funcionalidad, limitación que se pretende abordar en este Trabajo Fin de Máster. Una vez finalizado ese proyecto, uno de los principales puntos de mejora que se identificaron fue la posible mejora que el sistema experimentaría al poder hacer uso de unos recursos de computación más avanzados. Es por ello que se propone una forma de cómputo mucho más efectiva y que reduzca el trabajo del dispositivo móvil. Para ampliar el trabajo realizado, el primero de los objetivos de este proyecto será el desarrollo de una aplicación web, haciendo uso de algún framework de desarrollo web como VUE [5], que permita almacenar y gestionar las mediciones realizadas con la aplicación. También, cabe destacar que, la aplicación web permitirá la definición del preprocesamiento de los datos de una forma sencilla.

Así mismo, como segundo objetivo para la aplicación web, esta deberá permitir tanto la creación como la gestión de nuevos modelos de aprendizaje máquina [6]. Cabe destacar queestos modelos deben ser implementados en TensorFlow para que puedan ser usados por la aplicación si el usuario lo desea. Estos modelos se definirían en base a las diferentes arquitecturas e hiperparámetros indicados por el usuario de una forma sencilla. Tras esa definición, se entrenarían los modelos los cuales podrían ser analizados a posteriori visualizando los resultados más relevantes en la propia aplicación web. Finalizado el entrenamiento, se podrá subir un archivo con nuevas muestras a estimar y el servidor se encargará de devolver los resultados. A mayores el servidor se desarrollará siguiendo un esquema de microservicios para lo cual se utilizará como tecnología de virtualización docker [8] con el fin de dividir el sistema en diferentes módulos que pueden ser reutilizables, lo cual permitirá un fácil escalado de la aplicación tanto horizontal como verticalmente independientemente del sistema de alojamiento a usar, ya sea AWS, GCP o MS Azure. Es de destacar también la realización de una actualización en la aplicación móvil que, permitir la comunicación con el servidor para realizar tareas tales como: la descarga de modelos en el teléfono para su ejecución de forma local, ejecución de modelos para la generación de predicciones en el servidor o el intercambio de mediciones con el servidor. Por último, se pretende mejorar la usabilidad por parte del usuario.

Finalmente, con el fin de comprobar el funcionamiento, se realizará una nueva aplicación práctica en la que se realizará una toma de mediciones acerca de diferentes tipos de materiales para tratar de inferir alguna característica sobre los datos recogidos.

Bibliografía:
[1] Espectrofotómetro [Online], https://www.ecured.cu/Espectrofot%C3%B3metro
[2] Espectrometría de masas [Online], https://www.mncn.csic.es/docs/repositorio/es_ES/investigacion/cromatografia/espectrometria_de_masas.pdf
[3] Díaz, N. A., Ruiz, J. A. B., Reyes, E. F., Cejudo, A. G., Novo, J. J., Peinado, J. P., ... & Fiñana, I. T. (2010). Espectrofometría: Espectros de absorción y cuantificación colorimétrica de biomoléculas. sf http://www.uco.es/dptos/bioquimica-biolmol/pdfs/08_ESPECTROFOTOMETR%C3%8DA.pdf .
[4] Galdo, Brais (2018). Desarrollo de un laboratorio portátil basado en espectrometría de infrarrojo cercano. Traballo Fin de Grao. UDC.
[5] Evan You (2021) [Online] https://vuejs.org/
[6] Abadi, M., Barham, P., Chen, J., Chen, Z., Davis, A., Dean, J., ... & Kudlur, M. (2016). Tensorflow: A system for large-scale machine learning. In 12th {USENIX} symposium on operating systems design and implementation ({OSDI} 16) (pp. 265-283).
[7] Martín Abadi, Ashish Agarwal, Paul Barham, Eugene Brevdo, Zhifeng Chen, Craig Citro, Greg S. Corrado, Andy Davis, Jeffrey Dean, Matthieu Devin, Sanjay Ghemawat, Ian Goodfellow, Andrew Harp, Geoffrey Irving, Michael Isard, Rafal Jozefowicz, Yangqing Jia, Lukasz Kaiser, Manjunath Kudlur, Josh Levenberg, Dan Mané, Mike Schuster, Rajat Monga, Sherry Moore, Derek Murray, Chris Olah, Jonathon Shlens, Benoit Steiner, Ilya Sutskever, Kunal Talwar, Paul Tucker, Vincent Vanhoucke, Vijay Vasudevan, Fernanda Viégas, Oriol Vinyals, Pete Warden, Martin Wattenberg, Martin Wicke, Yuan Yu, and Xiaoqiang Zheng. TensorFlow: Large-scale machine learning on heterogeneous systems, 2015. Software available from tensorflow.org.
[8] Anderson, C. (2015). Docker [software engineering]. Ieee Software, 32(3), 102-c3.

## Motivación

La gran motivación de este proyecto es la continuación de un sistema con gran utilidad en el mundo actual. Tener la posibilidad de conocer al instante el material que compone unasustancia independientemente del lugar y momento en el que se tome la muestra, puede abrir numerosas posibilidades para la realización de investigaciones acerca de múltiples temas. Además, abre una la posibilidad a muchas empresas de realizar y optimizar sus tareas sin tener que depender de un laboratorio físico en el que realizar esas mediciones.

Destaca la posibilidad de realizar entrenamientos en cualquier momento y lugar, que otorga una gran flexibilidad a la hora de realizar las diferentes tareas de aprendizaje máquina. A mayores, es necesario subrayar la gran ventaja que supone delegar el cálculo de los diferentes modelos en un servidor. Este modelo de trabajo hace real la posibilidad de que cualquier persona con un teléfono móvil pueda realizar estas tareas de cómputo independientemente de que posea un ordenador adecuado para llevarlas a cabo.

## Objetivos

A modo general, los objetivos principales del proyecto serán:

* Implementación de un servidor web que permita la gestión tanto de mediciones como de modelos de aprendizaje máquina
* Implementación de un módulo dentro del servidor web para el entrenamiento de módulos de aprendizaje máquina.
* Implementación de un módulo dentro del servidor web para el preprocesado de las diferentes bases de datos.
* Realización de una aplicación práctica. Se desarrollará una base de datos que se usará para entrenar un modelo y realizar las predicciones correspondientes.

En cuanto a los objetivos secundarios se puede considerar:

* Mejora de usabilidad en la interfaz de la aplicación.
* Interfaz _responsive_ para la parte de la vista del servidor web.
* Automatismo de la inferencia de las proporciones de los datos con el fin de ajustarlo al modelo predictivo.

## Fases principales

A grandes rasgos existe la tentativa de realizar las siguientes fases en el proyecto:

1. Desarrollo del servidor web.
    1. Gestión de usuarios. Autenticación mediante nombre de usuario – contraseña.
    2. Almacenamiento de mediciones y modelos.
    3. Descarga de mediciones y modelos.
    4. Desarrollo y preparación de los datos de entrada.
    5. Desarrollo y entrenamiento de modelos desde el servidor.
    6. Visualización de las estadísticas principales de cada modelo.
    7. Ejecución de los modelos.
2. Mejora de la usabilidad en la aplicación Android.
3. Preparación para despliegue.
    1. Creación y gestión de los microservicios de los diferentes módulos del sistema.
4. Aplicación práctica.

Este proyecto será desarrollado haciendo uso del ciclo de vida en espiral [6]. En cada una de las fases se llevará a cabo un análisis exhaustivo acerca de los principales objetivos, se realizará el diseño de cada iteración y se realizará la implementación de la misma para finalizar con las pruebas necesarias.

Es necesario destacar que antes de comenzar una nueva iteración, se lleva a cabo una refactorización tanto del código como del diseño y los requisitos en función de las necesidades de la aplicación.

Bibliografía:

[9] Pressman, R. S., & Troya, J. M. (1988). Ingeniería del software.

## Herramientas y medios a utilizar

Para la realización de esta propuesta son necesarios los siguientes materiales:

* Un espectrofotómetro portátil el cual será facilitado por el laboratorio RNASA-IMEDIR.
* Un dispositivo móvil con las capacidades necesarias para la comunicación con el espectrofotómetro.
* Una cuenta de Google para el alojamiento del servidor.

## Justificación de la naturaleza profesional

Este proyecto tiene una gran aplicabilidad en el mundo real. Se podría conocer las propiedades de un material en cualquier lugar y momento con el dispositivo adecuado. Además, este proyecto abarca todas las características del desarrollo software, dado el tamaño y la complejidad del sistema a desarrollar. Se propone el desarrollo de un servidor web en el que realizar operaciones de almacenamiento y descarga de datos. Este servidor también dará la posibilidad de realizar  entrenamientos de modelos de aprendizaje máquina o pipelines de procesado de forma sencilla.

También se aprecia una tarea de investigación en la que poner a prueba las capacidades generales del sistema desarrollado. En esta tarea se tomarán muestras y se almacenarán en el servidor. Posteriormente, se hará uso de la base de datos desarrollada para realizar un entrenamiento con la misma y obtener un modelo con el que realizar estimaciones acerca de nuevos datos recogidos.

Es en este punto en el que es necesario destacar que se hará uso de una metodología bien definida y estructurada que permita garantizar la robustez del producto desarrollado minimizando el número de errores. Para ello se realizarán dentro de la metodología un conjunto de pruebas de manera recurrente con el fin de garantizar este punto. Dichas pruebas cubrirán los 4 aspectos fundamentales dentro de un sistema informático, es decir: pruebas unitarias, pruebas funcionales, pruebas de integración y pruebas de aceptación. Estas pruebas están destinadas a garantizar la calidad del producto desde el punto de vista del usuario, minimizando el número de fallos en el sistema y maximizando la facilidad de uso del mismo. A mayores la metodología facilitará realizar un seguimiento en el desarrollo del proyecto con el fin de controlar posibles desviaciones en tiempo o esfuerzo.

Este proyecto refleja en gran medida el conocimiento adquirido en el máster. Está ampliamente relacionada con asignaturas como Internet Como Servizo; se relaciona con el uso de tecnologías como docker o AWS, Diseño de Sistemas de Información; se relaciona con el diseño y la arquitectura del servidor, Calidad, Seguridad e Auditoría Informática; se relaciona con fases como la autenticación o securización del servidor, o Arquitectura e Plataformas Móviles; relacionada de forma muy directa con la parte Android del trabajo. Otras asignaturas a destacar podrían ser Interacción, Gráficos e Multimedia; se relaciona con la parte de visualización web o Dirección de Proxectos; relacionada directamente con la planificación de este proyecto.
