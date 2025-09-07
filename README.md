# 🎬 MyTube (Java & JavaFX)

**MyTube** es una aplicación de **escritorio** para reproducir **vídeo y audio** (estilo YouTube) desarrollada con **Java 17** y **JavaFX**.  
Sigue una **arquitectura MVC** con una separación clara entre **vista (FXML/CSS)**, **controladores** y **modelo/capa de datos**, priorizando código mantenible y fácil de extender.

---

## ✨ Características principales

- **Reproducción multimedia**: vídeo y audio con controles de `Play/Pause`, `Seek`, `Stop`, `Mute`, `Volumen`, `Velocidad` y **pantalla completa**.
- **Listas de reproducción**: crear, añadir, eliminar y reordenar elementos (cola de reproducción).
- **Interfaz responsive** con **FXML + CSS** y componentes reutilizables.

---

## 🧱 Arquitectura y diseño

- **Patrón MVC**:  
  - **Model**: entidades como `Multimedia`, `ListaArchivos`, `Reproductor`.  
  - **View**: FXML, layouts y estilos (CSS) desacoplados de la lógica.  
  - **Controller**: orquestación de eventos, binding con la vista y llamadas a servicios.

---

## 🛠️ Tecnologías

- **Lenguaje/Runtime**: Java 17+
- **UI**: JavaFX (FXML, CSS)
- **Build**: Maven (JavaFX Maven Plugin)  
- **Control de versiones**: Git
