# $Author: tmahr $
# $Rev: 6607 $
# $Date: 2020-07-01 10:54:43 +0200 (Mi, 01. Jul 2020) $

from Detektor import Detektor
# from Vector import Vector
from Welt import Welt

def main(xMin, xMax, yMin, yMax):
    welt = Welt(xMin, xMax, yMin, yMax, anzahlSensoren=5, wellengeschwindigkeit=1)
    welt.bebenAusloesen(zeit=0, amplitude=1)
    print(welt)
    print("Durchmesser der Welt: " + str(welt.durchmesser()))

    detektor = Detektor(welt, nt=100, nx=100, ny=100)

    detektor.detektieren(welt.liefereMessungen())

main(50, 100, 40, 90)