package com.booflamoo.eye;

import java.nio.file.Path;


/**
 * Search a directory for picture files that have the same picture.
 * Phase 1, identical files (size, hash, then bits by bits
 */
class PicHunt {
    String directory = null;

    public PicHunt(String dir, Boolean dryrun) {
        directory = dir;
    }


    void run() {
        //first scan the directory for files, recursively
        File dd = new File(directory);
        if (!dd.isDirectory)
            throw new RuntimeException("not a directory");
        File[] files = dd.listFiles();
        //2nd arrange files by size
        //3rd for all files of size x
        //  make hash
        //  compare to each prior file hash
        //    if same hash as prior file
        //      compare bits
        //        if bits same
        //          delete older
    }
}
