# uav-manager
This project is a UAV management system, you can upload the signal file(txt), then will parse the signals and you can find signal msg by id.

## How to run ?
- 1.Import project into eclipse or intellji.
- 2.Config jdk>=1.8.
- 3.Select Application.java run as java application,then you will see the message in the console.
- 4.Follow the console message, input a signals file path.
- 5.Follow the console message, input the msg id.
- 6.The msg info in the console.

## How to run unit test?
- 1.Import project into eclipse or intellij.
- 2.select project and click run all tests.

## Format for Signal File

      planeA 0 0 0
      planeA 0 0 0 1 1 1
      planeA 1 1 1 1 2 3
      ...
      
*   First line is the initial signal, has two parts, 'planeA' is the plane id, '0 0 0' is the initial coordinate.
*   From second line, each line is a movement signal, 'planeA' is the plane id, '1 1 1' is the previous coordinate, '1 2 3' is the offset.

## Output

- Valid Message -> PlaneId {MsgId} {CurrentCoordinate}, Example: planeA 1 1 2 3.
- Error Message -> Error: {MsgId}, Example: Error: 3.
- Not Found -> Cannot find {MsgId}, Example: Cannot find 4.
