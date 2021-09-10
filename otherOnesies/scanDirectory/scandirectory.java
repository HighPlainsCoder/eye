/* java single to list the files in a directory
 tobe with filters, sorting, etc



"""
item*_i.dds
whl*_i.dds
weap*_i.dds

veh*_key.dds

i_med_*.dds
"""

imagetype = "jpeg"
imagefix = "jpg"
driveletter = "d"

directory = "."
timesince = time.mktime(time.strptime("2000/6/2/12/36","%Y/%m/%d/%H/%M"))
##print timesince,time.localtime(timesince)

i = 1
while i< len(sys.argv) :
    if re.compile("^(/|-)(d|D)$").match(sys.argv[i]) :
        directory = sys.argv[i+1]
        i=i+1
    elif re.compile("^(/|-)(t|T)$").match(sys.argv[i]) :
        timesince=time.mktime(time.strptime(sys.argv[i+1],"%Y/%m/%d/%H/%M"))
        i=i+1
    elif re.compile("^(/|-)(j|J)$").match(sys.argv[i]) :
        imagetype = "jpeg"
        imagefix = "jpg"
    elif re.compile("^(/|-)(p|P)$").match(sys.argv[i]) :
        imagetype = "png"
        imagefix = "png"
    elif re.compile("^(/|-)(l|L)$").match(sys.argv[i]) :
        driveletter = sys.argv[i+1]
        i=i+1

    i=i+1

if not re.compile(":").match(directory): directory = driveletter+":"+directory

os.chdir(directory)
fileset = os.listdir(directory)
m1 = re.compile("^item.*_i.dds$")
m2 = re.compile("^whl.*_i.dds$")
m3 = re.compile("^weap.*_i.dds$")
m4 = re.compile("^veh.*_key.dds$")
m5 = re.compile("^i_med_.*.dds$")

program = "\""+driveletter+":\\vog\\1_code\\util\\ImageConverter\\nconvert\" -in dds -out "+imagetype

for filename in fileset:
    filename = filename.lower()
##    print filename, time.localtime(os.stat(filename).st_mtime)
##    print "---", filename[-6:], filename[:4], filename[:3], filename[-8:]
    if m1.match(filename) \
    or m2.match(filename) \
    or m2.match(filename) \
    or m4.match(filename) \
    or m5.match(filename) :
        if os.stat(filename).st_mtime > timesince:
            cmd = program + " -o \\"+imagefix+"images\\" + filename[:-4] + "."+imagefix+" " + filename
            print cmd , os.system( cmd )

 */
