package edu.orangecoastcollege.cs273.jkim428.cs273superheroes;

/**
 * Represents a Superhero for the purposes of the CS273 Superheroes, including the superhero's name,
 * superpower and one thing about him/her, and the file name (including path) for its image.
 *
 * @author Jiwoong Kim
 * @version 1.0
 */

public class Superhero {

    private String mName;
    private String mSuperpower;
    private String mOneThing;
    private String mFileName;

    /**
     * Instantiates a new Superhero given its name, superpower and one thing.
     * @param username The username of the superhero
     * @param name The name of the superhero
     * @param superpower The superpower of the superhero
     * @param oneThing One thing about the superhero
     */
    public Superhero(String username, String name, String superpower, String oneThing) {
        mName = name;
        mSuperpower = superpower;
        mOneThing = oneThing;
        mFileName = username + ".png";
    }

    /**
     * Gets the name of the superhero.
     * @return The name of the superhero
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the superpower of the superhero.
     * @return The superpower of the superhero
     */
    public String getSuperpower() {
        return mSuperpower;
    }

    /**
     * Gets one thing of the superhero.
     * @return One thing about the superhero
     */
    public String getOneThing() {
        return mOneThing;
    }

    /**
     * Gets the file name for the superhero's image.
     * @return The file name for superhero's image
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * Compares two Superheroes for equality based on name, superpower, one thing, and file name.
     * @param o The other superhero.
     * @return True if the superheroes are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Superhero superhero = (Superhero) o;

        if (!mName.equals(superhero.mName)) return false;
        if (!mSuperpower.equals(superhero.mSuperpower)) return false;
        if (!mOneThing.equals(superhero.mOneThing)) return false;
        return mFileName.equals(superhero.mFileName);
    }

    /**
     * Generates an integer based hash code to uniquely represent this superhero.
     * @return An integer based hash code to represent this superhero.
     */
    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + (mSuperpower != null ? mSuperpower.hashCode() : 0);
        result = 31 * result + (mOneThing != null ? mOneThing.hashCode() : 0);
        result = 31 * result + (mFileName != null ? mFileName.hashCode() : 0);
        return result;
    }

    /**
     * Generates a text based representation of this Superhero.
     * @return A text based representation of this Superhero.
     */@Override
    public String toString() {
        return "Superhero{" +
                "mName='" + mName + '\'' +
                ", mSuperpower='" + mSuperpower + '\'' +
                ", mOneThing='" + mOneThing + '\'' +
                ", mFileName='" + mFileName + '\'' +
                '}';
    }
}
