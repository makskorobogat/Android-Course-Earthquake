# $Author: tmahr $
# $Rev: 6573 $
# $Date: 2020-06-24 20:14:49 +0200 (Mi, 24. Jun 2020) $

import math
import sys
from Vector import Vector
import pyrebase



class Detektor:





    def __init__(self, welt, All, nt, nx, ny):
        self.welt = welt
        self.All = All
        self.nt = nt
        self.nx = nx
        self.ny = ny



    # Transformiert Weltkoordinaten in Rasterkoordinaten
   # def weltInRaster(self, pos):
    #    x = round(((pos.x - self.welt.xMin) / (self.welt.xMax - self.welt.xMin) * self.nx))
     #   y = round(((pos.y - self.welt.yMin) / (self.welt.yMax - self.welt.yMin) * self.ny))
     #   return x,y

    # Transformiert Rasterkoordinaten in Weltkoordinaten
   # def rasterInWelt(self, x, y):
    #    xWelt = self.welt.xMin + (self.welt.xMax - self.welt.xMin) * x / self.nx
     #   yWelt = self.welt.yMin + (self.welt.yMax - self.welt.yMin) * y / self.ny
      #  return Vector(xWelt, yWelt)

    def __xRasterInWelt(self,x):
        return self.welt.xMin + (self.welt.xMax - self.welt.xMin) * x / self.nx

    def __yRasterInWelt(self,y):
        return self.welt.yMin + (self.welt.yMax - self.welt.yMin) * y / self.ny

    def __tRasterInWelt(self,tRaster,tWeltMin,tWeltMax):
        return tWeltMin + tRaster * (tWeltMax-tWeltMin) / self.nt

    def __maxZeitInMessungen(self, stosswellenmessungen):
        t = []
        for m in stosswellenmessungen:
            t.append(m.zeit)
        return max(t)

    def detektieren(self, stosswellenmessungen):

        tWeltMax = self.__maxZeitInMessungen(stosswellenmessungen)
        tWeltMin = tWeltMax - self.welt.durchmesser() / self.welt.v
        #print("tWeltMin=" + str(tWeltMin) + " tWeltMax="+str(tWeltMax))

        dtQuadratischeAbweichungOpt = sys.float_info.max
        tEpiOpt = None
        xEpiOpt = None
        yEpiOpt = None
        #print (stosswellenmessungen)
        # angenommenes Epizentrum: tEpi, xEpi, yEpi


        for tRaster in range(0, self.nt+1):
            tEpi = self.__tRasterInWelt(tRaster,tWeltMin,tWeltMax)
            #print("tRaster=" + str(tRaster) + " tWelt=" + str(tWelt))

            for xRaster in range(0, self.nx+1):
                xEpi = self.__xRasterInWelt(xRaster)
                #print (xEpi)
                for yRaster in range(0, self.ny+1):
                    yEpi = self.__yRasterInWelt(yRaster)
                    #print (yEpi)
                    dtQuadratischeAbweichung = 0

                    for m in stosswellenmessungen:
                        dx = m.position.x - xEpi
                        #print (dx, m.position.x, xEpi)
                        dy = m.position.y - yEpi
                        #print(dy, m.position.y, yEpi)
                        d = math.sqrt(dx*dx + dy*dy)
                        t = tEpi + d/self.welt.v
                        dt = m.zeit - t
                        #print (dt)
                        dtQuadratischeAbweichung += dt*dt
                    if dtQuadratischeAbweichung < dtQuadratischeAbweichungOpt:
                        dtQuadratischeAbweichungOpt = dtQuadratischeAbweichung
                        tEpiOpt = tEpi
                        xEpiOpt = xEpi
                        yEpiOpt = yEpi

                        #print("dtQuadratischeAbweichungOpt=" + str(dtQuadratischeAbweichungOpt) + " t=" + str(tEpiOpt) + " pos=" + str(xEpiOpt))


        posEpiOpt = Vector(xEpiOpt, yEpiOpt)
        print("Detektion: t=" + str(tEpiOpt) + " pos=" + str(posEpiOpt))

        #def take(latitude, longtude):
        #    return latitude, longtude

        #make = take (latitude, longtude)

        firebaseConfig = {'apiKey': "AIzaSyB7rFWYbOxMlUQlzkB-1cjEWmKvpTmIPhs",
                              'authDomain': "schockwellennetz-a62d7.firebaseapp.com",
                              'databaseURL': "https://schockwellennetz-a62d7-default-rtdb.europe-west1.firebasedatabase.app",
                              'projectId': "schockwellennetz-a62d7",
                              'storageBucket': "schockwellennetz-a62d7.appspot.com",
                              'messagingSenderId': "66216452714",
                              'appId': "1:66216452714:web:faa2860aa81c8a8ac06013",
                              'measurementId': "G-J2FX0YE3L6"}

        firebase = pyrebase.initialize_app(firebaseConfig)

        db = firebase.database()
        data = {'latitude': xEpiOpt, 'longtude': yEpiOpt}
        db.child("epizentrum").set(data)

