# NIR Lab Project

Continuación del trabajo fin de grado. Proyecto de desarrollo en investigación.

## Antes de comenzar...

Compilar la versión 1.0 de la aplicación e instalarla con el objetivo de comprobar que todo se encuentra correctamente.

Ajuste de los layouts del sistema para hacer uso de Constraints en lugar de Relative layout con el objetivo de mejorar la visualización y de la aplicación.

## Descripción del proyecto

Cada material posee una **composición molecular** específica que lo distingue del resto. Una forma de medir esta composición es a través de un espectrofotómetro [1]. Este dispositivo es capaz de analizar las diferentes estructuras moleculares en búsqueda de enlaces de carbono para tratar de identificar sus propiedades principales. La gran ventaja sobre la espectrometría de masas [2] es que esta es una **técnica no destructiva**, es decir, no destruye la muestra a la hora de analizarla. Esto es una gran ventaja puesto que permite realizar todas las mediciones que sean necesarias sobre una muestra [3].

Estos dispositivos han ido evolucionando en precisión, complejidad y tamaño, llegando a alcanzar bandas muy bajas (en torno a los 900 nm) y con menor tamaño. Si bien para realizar un análisis muy complejo es necesario un espectrofotómetro de gran tamaño y gran complejidad para obtener unas mediciones lo más precisas posibles, para un análisis más liviano es suficiente con el uso de un dispositivo de menor tamaño. Estos últimos también pueden ser utilizados para realizar **mediciones a pie de campo**, cuando no existe la posibilidad de transportar la muestra hasta el laboratorio.

A lo largo del desarrollo del Trabajo Fin de Grado [4] se ha desarrollado una aplicación capaz de conectarse con uno de estos dispositivos para tomar mediciones a pie de campo haciendo uso de la *tecnología BLE*. También se ha llevado a cabo la implementación de una forma de almacenar las muestras en formato JSON y se han implementado funcionalidades de predicción de características basadas en *TensorFlow Lite*. Esta última funcionalidad se ha complementado con una tarea de investigación para comprobar el correcto funcionamiento de la característica.

Los principales objetivos de esta continuación sería la implementación de la comunicación haciendo uso de la **tecnología USB** así como el desarrollo de un **servidor web** en el que almacenar mediciones así como modelos de aprendizaje máquina [5] que podrán ser descargados en el teléfono con el objetivo de comprobar su funcionamiento. Además, en este servidor se permitirá entrenar los diferentes modelos haciendo uso de las bases de datos correspondientes. A mayores, también se realizará una nueva **aplicación práctica** en el que se realizará una toma de mediciones acerca de un tema para tratar de inferir alguna característica sobre los datos recogidos.

Bibliografía:

[1] Espectrofotómetro [Online], *https://www.ecured.cu/Espectrofot%C3%B3metro*
[2] Espectrometría de masas [Online], *https://www.mncn.csic.es/docs/repositorio/es_ES/investigacion/cromatografia/espectrometria_de_masas.pdf*
[3] Díaz, N. A., Ruiz, J. A. B., Reyes, E. F., Cejudo, A. G., Novo, J. J., Peinado, J. P., ... & Fiñana, I. T. (2010). Espectrofometría: Espectros de absorción y cuantificación colorimétrica de biomoléculas. sf *http://www.uco.es/dptos/bioquimica-biolmol/pdfs/08_ESPECTROFOTOMETR%C3%8DA.pdf* (último acceso: 9 de febrero de 2016).
[4] Galdo, Brais (2018). *Desarrollo de un laboratorio portátil basado en espectrometría de infrarrojo cercano*
[5] Abadi, M., Barham, P., Chen, J., Chen, Z., Davis, A., Dean, J., ... & Kudlur, M. (2016). *Tensorflow: A system for large-scale machine learning. In 12th {USENIX} symposium on operating systems design and implementation* ({OSDI} 16) (pp. 265-283).

## Motivación

La gran motivación de este proyecto es la continuación de un sistema con gran utilidad en el mundo actual. Tener la posibilidad de conocer al instante el material que compone una sustancia independientemente del lugar y momento en el que se tome la muestra puede abrir numerosas posibilidades para la realización de investigaciones acerca de múltiples temas así como para una gran cantidad de empresas a la hora de realizar sus tareas sin tener que depender de un laboratorio físico en el que realizar esas mediciones.

Destaca la posibilidad de realizar entrenamientos en cualquier momento y lugar, que otorga una gran flexibilidad a la hora de realizar las diferentes tareas de aprendizaje máquina. A mayores, es necesario subrayar la gran ventaja que supone delegar el cálculo de los diferentes modelos en un servidor dado que con este modelo de trabajo hace real la posibilidad de que cualquier persona con un teléfono móvil pueda realizar estas tareas de cómputo independientemente de que posea un ordenador adecuado para llevarlas a cabo.

Por último, gracias al desarrollo del servidor de almacenamiento de mediciones y modelos se optimiza el uso de recursos de almacenamiento del teléfono móvil puesto que estos datos estarán almacenados en el servidor.

## Objetivos

A modo general, los objetivos principales del proyecto serán:

* Implementación de la comunicación teléfono móvil – dispositivo mediante el protocolo USB.
* Implementación de un servidor web que permita tanto el almacenamiento como la descarga de mediciones y modelos de aprendizaje máquina así como el entrenamiento de estos últimos.
* Realización de una aplicación práctica. Se desarrollará una base de datos que se usará para entrenar un modelo y realizar las predicciones correspondientes.

En cuanto a los objetivos secundarios se puede considerar:

* Mejora de usabilidad en la interfaz de la aplicación.
* Gestión de protocolos USB – BLE.
* Interfaz adaptable a cada dispositivo en el portal web del servidor.
* Capacidad de ajustar la entrada de los datos en el modelo predictivo.

## Fases principales

A grandes rasgos existe la tentativa de realizar las siguientes fases en el proyecto:

1. Desarrollo de la comunicación haciendo uso de la tecnología USB:
    1. Obtención de la información del espectrofotómetro.
    2. Realización de mediciones.
2. Desarrollo del servidor web.
    1. Gestión de usuarios. Autenticación mediante nombre de usuario – contraseña.
    2. Almacenamiento de mediciones y modelos TFLite.
    3. Descarga de mediciones y modelos TFLite.
    4. Entrenamiento de modelos desde el servidor.
3. Aplicación práctica.
4. Preparación para despliegue.

Este proyecto será desarrollado haciendo uso del ciclo de vida en espiral [6]. En cada una de las fases se llevará a cabo un análisis exhaustivo acerca de los principales objetivos, se realizará el diseño de cada iteración y se realizará la implementación de la misma para finalizar con las pruebas necesarias.

Es necesario destacar que antes de comenzar una nueva iteración, se lleva a cabo una refactorización tanto del código como del diseño y los requisitos en función de las necesidades de la aplicación.

Bibliografía:

[6] Pressman, R. S., & Troya, J. M. (1988). Ingeniería del software.

## Herramientas y medios a utilizar

Para la realización de esta propuesta son necesarios los siguientes materiales:

* Un espectrofotómetro portátil el cual será facilitado por el laboratorio RNASA-IMEDIR.
* Un dispositivo móvil con las capacidades necesarias para la comunicación con el espectrofotómetro.
* Una cuenta de Google para el alojamiento del servidor.

## Justificación de la naturaleza profesional

Este proyecto tiene una gran aplicabilidad en el mundo real. Se podría conocer las propiedades de un material en cualquier lugar y momento con el dispositivo adecuado. Además, este proyecto abarca todas las características del desarrollo software, dado el tamaño y la complejidad del sistema a desarrollar. A mayores del desarrollo para el sistema operativo Android, se propone el desarrollo de un servidor web en el que realizar operaciones de almacenamiento y descarga de datos así como entrenamientos de modelos de aprendizaje máquina.

También se aprecia una tarea de investigación en la que poner a prueba las capacidades generales del sistema desarrollado. En esta tarea se tomarán muestras haciendo uso del nuevo método de comunicación implementado y se almacenarán en el servidor. Posteriormente, se hará uso de la base de datos desarrollada para realizar un entrenamiento con la misma y obtener un modelo con el que realizar estimaciones acerca de nuevos datos recogidos.

