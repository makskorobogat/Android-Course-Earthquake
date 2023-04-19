# $Author: tmahr $
# $Rev: 6573 $
# $Date: 2020-06-24 20:14:49 +0200 (Mi, 24. Jun 2020) $

import math
import sys
from Vector import Vector
import time

class Detektor:

    def __init__(self, welt, All, nt, nx, ny):
        self.welt = welt
        self.All = All
        self.nt = nt
        self.nx = nx
        self.ny = ny
        #print (welt, nt, nx, ny)
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

    def __xRasterInWelt1(self,x):
        return self.welt.xAll[1] + (self.welt.xAll[0] - self.welt.xAll[1]) * x / self.nx

    def __xRasterInWelt2(self,x):
        return self.welt.xAll[3] + (self.welt.xAll[2] - self.welt.xAll[3]) * x / self.nx

    def __xRasterInWelt3(self,x):
        return self.welt.xAll[5] + (self.welt.xAll[4] - self.welt.xAll[5]) * x / self.nx

    def __xRasterInWelt4(self,x):
        return self.welt.xAll[7] + (self.welt.xAll[6] - self.welt.xAll[7]) * x / self.nx


    def __yRasterInWelt1(self,y):
        return self.welt.yAll[1] + (self.welt.yAll[0] - self.welt.yAll[1]) * y / self.ny

    def __yRasterInWelt2(self,y):
        return self.welt.yAll[3] + (self.welt.yAll[2] - self.welt.yAll[3]) * y / self.ny

    def __yRasterInWelt3(self,y):
        return self.welt.yAll[5] + (self.welt.yAll[4] - self.welt.yAll[5]) * y / self.ny

    def __yRasterInWelt4(self,y):
        return self.welt.yAll[7] + (self.welt.yAll[6] - self.welt.yAll[7]) * y / self.ny

    def __tRasterInWelt(self,tRaster,tWeltMin,tWeltMax):
        return tWeltMin + tRaster * (tWeltMax-tWeltMin) / self.nt

    def __maxZeitInMessungen(self, stosswellenmessungen):
        t = []
        for m in stosswellenmessungen:
            t.append(m.zeit)
        return max(t)

    def detektieren(self, stosswellenmessungen, All):
        tWeltMax = self.__maxZeitInMessungen(stosswellenmessungen)
        g = 0
        tWeltMin = tWeltMax - self.welt.durchmesser(g) / self.welt.v
        #print("tWeltMin=" + str(tWeltMin) + " tWeltMax="+str(tWeltMax))

        dtQuadratischeAbweichungOpt = sys.float_info.max
        tEpiOpt = None
        xEpiOpt = None
        yEpiOpt = None
        #j = 0
        # angenommenes Epizentrum: tEpi, xEpi, yEpi
        for tRaster in range(0, self.nt+1):
            tEpi = self.__tRasterInWelt(tRaster,tWeltMin,tWeltMax)
            #print("tRaster=" + str(tRaster) + " tWelt=" + str(tWelt))

            for xRaster in range(0, self.nx+1):
                xEpi1 = self.__xRasterInWelt1(xRaster)
                xEpi2 = self.__xRasterInWelt2(xRaster)
                xEpi3 = self.__xRasterInWelt3(xRaster)
                xEpi4 = self.__xRasterInWelt4(xRaster)
                xEpi = [0, xEpi1,xEpi2,xEpi3,xEpi4]
                #print(xRaster)

                for yRaster in range(0, self.ny+1):
                    yEpi1 = self.__yRasterInWelt1(yRaster)
                    yEpi2 = self.__yRasterInWelt2(yRaster)
                    yEpi3 = self.__yRasterInWelt3(yRaster)
                    yEpi4 = self.__yRasterInWelt4(yRaster)
                    yEpi = [0, yEpi1,yEpi2,yEpi3,yEpi4]
                    #print (yEpi)

                    #print (yRaster)
                    j = 0
                    l = 8
                    h = 0
                    f = 0
                    r = 4
                    #print(dx, m.position.x, xEpi)
                    dtQuadratischeAbweichung = 0

                    #print(f)
                    #print(h)
                    #dx = All[f] - xEpi[j]
                    #print(dx, All[f], xEpi[j])
                    #dy = All[r] - yEpi[h]
                    #print(dy, All[r], yEpi[h])
                    for m in range (0, 4):
                        dx = All[f] - xEpi[j]
                        #print(dx, All[f], xEpi[j])
                        dy = All[r] - yEpi[h]
                        d = math.sqrt(dx*dx + dy*dy)
                        t = tEpi + d/self.welt.v
                        dt = All[l] - t
                        #print (dt)
                        h += 1
                        j += 1
                        l += 1
                        f += 1
                        r += 1
                        #time.sleep(0.01)
                        dtQuadratischeAbweichung += dt*dt


                if dtQuadratischeAbweichung < dtQuadratischeAbweichungOpt:
                    dtQuadratischeAbweichungOpt = dtQuadratischeAbweichung
                    tEpiOpt = tEpi
                    xEpiOpt = xEpi[1]
                    yEpiOpt = yEpi[1]
                    #print("dtQuadratischeAbweichungOpt=" + str(dtQuadratischeAbweichungOpt) + " t=" + str(tEpiOpt) + " pos=" + str(xEpiOpt))

        posEpiOpt = Vector(xEpiOpt, yEpiOpt)
        print("Detektion: t=" + str(tEpiOpt) + " pos=" + str(posEpiOpt))
