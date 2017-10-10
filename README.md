# Axiom

- Main game program
The actual game logic has to be implemented by some algorithms. It is distinct from any rendering, sound or input work.

- Rendering engine
The rendering engine generates 3D animated graphics by the chosen method (rasterization, ray-tracing or any different technique).

  - Instead of being programmed and compiled to be executed on the CPU or GPU directly, most often rendering engines are built upon one or multiple rendering application programming interfaces (APIs), such as Direct3D or OpenGL which provide a software abstraction of the graphics processing unit (GPU).

  - Low-level libraries such as DirectX, Simple DirectMedia Layer (SDL), and OpenGL are also commonly used in games as they provide hardware-independent access to other computer hardware such as input devices (mouse, keyboard, and joystick), network cards, and sound cards. Before hardware-accelerated 3D graphics, software renderers had been used. Software rendering is still used in some modeling tools or for still-rendered images when visual accuracy is valued over real-time performance (frames-per-second) or when the computer hardware does not meet needs such as shader support.

  - With the advent of hardware accelerated physics processing, various physics APIs such as PAL and the physics extensions of COLLADA became available to provide a software abstraction of the physics processing unit of different middleware providers and console platforms.

  - Game engines can be written in any programming language like C++, C or Java, though each language is structurally different and may provide different levels of access to specific functions.

- Audio engine
The audio engine is the component which consists of algorithms related to sound. It can calculate things on the CPU, or on a dedicated ASIC. Abstraction APIs, such as OpenAL, SDL audio, XAudio 2, Web Audio, etc. are available.

- Physics engine
The physics engine is responsible for emulating the laws of physics realistically within the application.

- Artificial intelligence
The AI is usually outsourced from the main game program into a special module to be designed and written by software engineers with specialist knowledge.

