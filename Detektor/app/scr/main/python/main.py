# $Author: tmahr $
# $Rev: 6607 $
# $Date: 2020-07-01 10:54:43 +0200 (Mi, 01. Jul 2020) $

from Detektor import Detektor
#from Vector import Vector
from Welt import Welt

welt = Welt(xMin=0.0, xMax=10.0, yMin=0.0, yMax=10.0, anzahlSensoren=5, wellengeschwindigkeit=1)
welt.bebenAusloesen(zeit=0, amplitude=1)
print(welt)
print("Durchmesser der Welt: " + str(welt.durchmesser()))

detektor = Detektor(welt, nt=100, nx=100, ny=100)

# Teste Raster-Projektion
#pos = Vector(4.4,6.9)
#x,y = detektor.weltInRaster(pos)
#pos2 = detektor.rasterInWelt(x,y)
#print(str(pos) + " -> " + str(x) + " " + str(y) + " -> " + str(pos2))

detektor.detektieren(welt.liefereMessungen())
