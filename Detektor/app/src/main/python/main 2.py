# $Author: tmahr $
# $Rev: 6607 $
# $Date: 2020-07-01 10:54:43 +0200 (Mi, 01. Jul 2020) $

from Detektor3 import Detektor
# from Vector import Vector
from Welt2 import Welt

def main(x1,y1,x2,y2,x3,y3,epi1,epi2):
    x1max = x1 + 0.01
    x1min = x1 - 0.01
    x2max = x2 + 0.01
    x2min = x2 - 0.01
    x3max = x3 + 0.01
    x3min = x3 - 0.01

    y1max = y1 + 0.01
    y1min = y1 - 0.01
    y2max = y2 + 0.01
    y2min = y2 - 0.01
    y3max = y3 + 0.01
    y3min = y3 - 0.01

    t1 = 0
    t2 = 0
    t3 = 0
    t4 = 0

    xAll = [x1max,x1min,x2max,x2min,x3max,x3min]
    yAll = [y1max,y1min,y2max,y2min,y3max,y3min]

    All = [x1,x2,x3,y1,y2,y3, t1, t2, t3 ,t4]

    xMax = max(xAll)
    xMin = min(xAll)
    yMax = max(yAll)
    yMin = min(yAll)

    print (xMax)
    print (xMin)



    epi1 = epi1
    epi2 = epi2
   # print (x1,x2,x3,x4,y1,y2,y3,y4)
    #print (xAll)
    #print(yAll)

    welt = Welt(xAll, yAll, epi1, epi2, xMax, xMin, yMax, yMin, anzahlSensoren=5, wellengeschwindigkeit=1)
    welt.bebenAusloesen(All, zeit=0, amplitude=1)
    print(welt)
    #print("Durchmesser der Welt: " + str(welt.durchmesser()))

    detektor = Detektor(welt, All, nt=100, nx=100, ny=100)

    # Teste Raster-Projektion
    #pos = Vector(4.4,6.9)
    #x,y = detektor.weltInRaster(pos)
    #pos2 = detektor.rasterInWelt(x,y)
    #print(str(pos) + " -> " + str(x) + " " + str(y) + " -> " + str(pos2))
    #print (welt.liefereMessungen())
    detektor.detektieren(welt.liefereMessungen())


#main(49.4677, 11.098, 51.37, 12.393, 50.116, 8.671, 48.767, 9.172, 51.318, 9.489)